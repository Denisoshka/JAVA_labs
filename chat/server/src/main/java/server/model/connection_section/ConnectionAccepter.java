package server.model.connection_section;

import dto.DTOConverterManager;
import dto.RequestDTO;
import dto.exceptions.UnableToSerialize;
import dto.interfaces.DTOConverterManagerInterface;
import dto.subtypes.login.*;
import io_processing.IOProcessor;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import server.model.Server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ConnectionAccepter implements Runnable {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(ConnectionAccepter.class);

  public enum RegistrationState {
    LOGIN_SUCCESS("Success"),
    USER_NOT_LOGIN("User not login"),
    TIMEOUT_EXPIRED("Timeout expired"),
    INCORRECT_NAME("Incorrect name data"),
    INCORRECT_PASSWORD("Incorrect password"),
    INCORRECT_LOGIN_REQUEST("Incorrect login request"),
    LOGIN_FAILED("Login failed"),
    ;

    final private String description;

    RegistrationState(String desc) {
      this.description = desc;
    }

    public String getDescription() {
      return description;
    }
  }

  private static long MAX_CONNECTION_TIME = 30_000;

  private final Server server;
  private final LoginDTOConverter converter;
  private final DTOConverterManager manager;
  private final IOProcessor ioProcessor;
  private final ChatUsersDAO chatUsersDAO;

  /**
   * close {@code socket} if exceptions occurs during {@code run()} or {@code ConnectionAccepter()} else submit new connection to {@code server} connections
   */
  public ConnectionAccepter(Server server, Socket socket) {
    IOProcessor tmp = null;
    try {
      tmp = new IOProcessor(socket, 10_000);
    } catch (IOException e) {
      try {
        socket.close();
      } catch (IOException _) {
      }
      log.error(e.getMessage(), e);
    }
    ioProcessor = tmp;
    this.server = server;
    this.chatUsersDAO = server.getChatUsersDAO();
    this.manager = server.getConverterManager();
    this.converter = (LoginDTOConverter) manager.getConverterBySection(RequestDTO.DTO_SECTION.LOGIN);
  }

  @Override
  public void run() {
    if (ioProcessor == null) {
      return;
    }
    long startTime = System.currentTimeMillis();
    long now = startTime;
    try {
      for (; !Thread.currentThread().isInterrupted() && now - startTime <= MAX_CONNECTION_TIME;
           now = System.currentTimeMillis()) {
        Document root = null;
        try {
          byte[] bytemsg = ioProcessor.receiveMessage();
          root = manager.getXMLTree(bytemsg);
          var ret = handleLoginRequest(root);
          if (ret == RegistrationState.LOGIN_SUCCESS) {
            return;
          }
        } catch (SocketTimeoutException _) {
          if (System.currentTimeMillis() - startTime > MAX_CONNECTION_TIME) {
            log.info("connection time expired");
            break;
          }
        } catch (UnableToSerialize e) {
          log.error(e.getMessage(), e);
          try {
            ioProcessor.sendMessage(manager.serialize(new LoginError(e.getMessage())).getBytes());
          } catch (UnableToSerialize e1) {
            log.warn(e1.getMessage(), e1);
          }
        }
      }
      onLoginTimeExpired();
    } catch (IOException _) {
    }
  }

  private RegistrationState handleLoginRequest(Document root) throws IOException {
    RequestDTO.COMMAND_TYPE commandType = DTOConverterManagerInterface.getDTOCommand(root);
    RegistrationState ret;
    if (commandType != RequestDTO.COMMAND_TYPE.LOGIN) {
      return RegistrationState.INCORRECT_LOGIN_REQUEST;
    }

    LoginCommand command = (LoginCommand) converter.deserialize(root);
    ChatUserEntity chatUser = chatUsersDAO.findAccountByUserName(command.getName());
    if (chatUser != null && chatUser.getPasswordHash() == command.getPassword().hashCode()) {
      ret = RegistrationState.LOGIN_SUCCESS;
    } else if (chatUser != null && chatUser.getPasswordHash() != command.getPassword().hashCode()) {
      ret = RegistrationState.INCORRECT_PASSWORD;
    } else {
      Long userId = chatUsersDAO.saveAccount(new ChatUserEntity(command.getName(), command.getPassword().hashCode()));
      if (userId != null) {
        ret = RegistrationState.LOGIN_SUCCESS;
      } else {
        ret = RegistrationState.LOGIN_FAILED;
      }
    }
    if (ret == RegistrationState.LOGIN_SUCCESS) {
      onLoginSuccess(ioProcessor, chatUsersDAO.findAccountByUserName(command.getName()));
    } else {
      ioProcessor.sendMessage(converter.serialize(new LoginError(ret.description)).getBytes());
    }
    return ret;
  }

  private void onLoginSuccess(IOProcessor ioProcessor, ChatUserEntity entity) throws IOException {
    byte[] successResponse = converter.serialize(new LoginSuccess(entity.getAvatar(), entity.getAvatarMimeType())).getBytes();
    byte[] loginEvent = converter.serialize(
            new LoginEvent(entity.getUserName(), entity.getAvatar(), entity.getAvatarMimeType())
    ).getBytes();
    ioProcessor.sendMessage(successResponse);
    for (var conn : server.getConnections()) {
      try {
        conn.sendMessage(loginEvent);
      } catch (IOException _) {
        server.submitExpiredConnection(conn);
      }
    }
    server.submitNewConnection(new ServerConnection(ioProcessor, entity.getUserName()));
  }

  private void onLoginTimeExpired() {
    try {
      byte[] errmsg = converter.serialize(new LoginError("login time expired")).getBytes();
      ioProcessor.sendMessage(errmsg);
    } catch (UnableToSerialize e) {
      log.warn(e.getMessage(), e);
    } catch (IOException _) {
    } finally {
      try {
        ioProcessor.close();
      } catch (IOException _) {
      }
    }
  }
}
