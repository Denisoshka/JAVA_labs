package server.model.connection_section;

import dto.DTOConverterManager;
import dto.RequestDTO;
import dto.exceptions.UnableToSerialize;
import dto.interfaces.DTOConverterManagerInterface;
import dto.subtypes.LoginDTO;
import dto.subtypes.MessageDTO;
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

  public static long MAX_CONNECTION_TIME = 30_000;
  private final Server server;
  private final LoginDTO.LoginDTOConverter converter;
  private final DTOConverterManager manager;
  private final IOProcessor ioProcessor;
  private RegistrationState registrationState = null;
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
    this.converter = (LoginDTO.LoginDTOConverter) manager.getConverterBySection(RequestDTO.DTO_SECTION.LOGIN);
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
          RequestDTO.DTO_SECTION section = DTOConverterManagerInterface.getDTOSection(root);
          RequestDTO.COMMAND_TYPE commandType = DTOConverterManagerInterface.getDTOCommand(root);
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
            ioProcessor.sendMessage(manager.serialize(new LoginDTO.Error(e.getMessage())).getBytes());
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
    RequestDTO.DTO_SECTION section = DTOConverterManagerInterface.getDTOSection(root);
    RequestDTO.COMMAND_TYPE commandType = DTOConverterManagerInterface.getDTOCommand(root);
    RegistrationState ret;
    if (section != RequestDTO.DTO_SECTION.LOGIN || commandType != RequestDTO.COMMAND_TYPE.LOGIN) {
      ret = RegistrationState.USER_NOT_LOGIN;
    }

    LoginDTO.Command command = (LoginDTO.Command) converter.deserialize(root);
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
      onLoginSuccess(ioProcessor, command.getName());
    } else {
      ioProcessor.sendMessage(converter.serialize(new LoginDTO.Error(ret.description)).getBytes());
    }
    return ret;
  }

  private void onLoginSuccess(IOProcessor ioProcessor, String name) throws IOException {
    byte[] successResponse = converter.serialize(new LoginDTO.Success()).getBytes();
    byte[] loginEvent = converter.serialize(new LoginDTO.Event(name)).getBytes();
    ioProcessor.sendMessage(successResponse);
    for (var conn : server.getConnections()) {
      try {
        conn.sendMessage(loginEvent);
      } catch (IOException _) {
        server.submitExpiredConnection(conn);
      }
    }
    server.submitNewConnection(new ServerConnection(ioProcessor, name));
  }

  private void onLoginTimeExpired() {
    try {
      byte[] errmsg = converter.serialize(new LoginDTO.Error("login time expired")).getBytes();
      ioProcessor.sendMessage(errmsg);
    } catch (UnableToSerialize e) {
      log.warn(e.getMessage(), e);
    } catch (IOException _) {
    } finally {
      try {
        shutdownIO();
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  private void shutdownIO() throws IOException {
    ioProcessor.close();
  }
}
