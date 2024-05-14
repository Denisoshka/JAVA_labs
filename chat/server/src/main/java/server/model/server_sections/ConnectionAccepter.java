package server.model.server_sections;

import dto.DTOConverterManager;
import dto.RequestDTO;
import dto.exceptions.UnableToSerialize;
import dto.subtypes.LoginDTO;
import io_processing.IOProcessor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import server.model.Server;
import server.model.io_processing.ServerConnection;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

@Slf4j
public class ConnectionAccepter implements Runnable {
  public enum RegistrationState {
    SUCCESS("Success"),
    TIMEOUT_EXPIRED("Timeout expired"),
    INCORRECT_NAME_DATA("Incorrect name data"),
    INCORRECT_PASSWORD_DATA("Incorrect password data"),
    INCORRECT_PASSWORD("Incorrect password"),
    INCORRECT_LOGIN_REQUEST("Incorrect login request");

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
  private IOProcessor ioProcessor;
  private RegistrationState registrationState = null;
  private Socket socket;

  /**
   * close {@code socket} if exceptions occurs during {@code run()} or {@code ConnectionAccepter()} else submit new connection to {@code server} connections
   */

  public ConnectionAccepter(Server server, Socket socket) {
    try {
      this.ioProcessor = new IOProcessor(socket, 10_000);
    } catch (IOException _) {
      try {
        shutdownIO();
      } catch (IOException _) {
      }
    }
    this.socket = socket;
    this.server = server;
    this.manager = server.getConverterManager();
    this.converter = (LoginDTO.LoginDTOConverter) manager.getConverter(RequestDTO.DTO_SECTION.LOGIN);
  }

  @Override
  public void run() {
    log.info("ConnectionAccepter started");
    if (ioProcessor == null) {
      log.warn("IOProcessor is null");
      return;
    }
    long startTime = System.currentTimeMillis();
    long now = startTime;
    Logger moduleLogger = server.getModuleLogger();
    try {
      for (; !Thread.currentThread().isInterrupted() && now - startTime <= MAX_CONNECTION_TIME;
           now = System.currentTimeMillis()) {
        Document root;
        try {
          root = manager.getXMLTree(ioProcessor.receiveMessage());
          RequestDTO.DTO_SECTION dtoSection = manager.getDTOSection(root);
          RequestDTO.DTO_TYPE dtoType = manager.getDTOType(root);
          if ((registrationState = handleLoginRequest(dtoSection, dtoType, root)) == RegistrationState.SUCCESS) {
            break;
          }
        } catch (UnableToSerialize e) {
          log.info(e.getMessage(), e);
          ioProcessor.sendMessage(converter.serialize(new LoginDTO.Error("unable to serialize received message")).getBytes());
        } catch (SocketTimeoutException _) {
          if (System.currentTimeMillis() - startTime > MAX_CONNECTION_TIME) {
            log.info("connection time expired");
            break;
          }
        }
      }
      if (registrationState != RegistrationState.SUCCESS) onLoginExpired();
    } catch (IOException e) {
      log.warn(e.getMessage(), e);
      try {
        onLoginExpired();
      } catch (IOException _) {
      }
    } catch (Exception e) {
      log.warn(e.getMessage(), e);
    }
  }

  private RegistrationState handleLoginRequest(RequestDTO.DTO_SECTION dtoSection, RequestDTO.DTO_TYPE dtoType, Node root) throws IOException {
    if (dtoSection != RequestDTO.DTO_SECTION.LOGIN || dtoType != RequestDTO.DTO_TYPE.COMMAND) {
      var errMsg = STR."excepted name=login actual: \{dtoType}\n excepted type=command actual: \{dtoSection}";
      server.getModuleLogger().info(errMsg);
      ioProcessor.sendMessage(converter.serialize(new LoginDTO.Error(
              errMsg)).getBytes()
      );
      return RegistrationState.INCORRECT_LOGIN_REQUEST;
    }
    LoginDTO.Command command = (LoginDTO.Command) converter.deserialize(root);
    String name = command.getName();
    int passwordHash = command.getPassword().hashCode();
    Integer registeredPasswordHash = server.getRegisteredUsers().get(name);

    if (registeredPasswordHash != null && passwordHash != registeredPasswordHash) {
      var errMsg = "incorrect password";
      server.getModuleLogger().info(errMsg);
      ioProcessor.sendMessage(converter.serialize(new LoginDTO.Error(errMsg)).getBytes());
      return RegistrationState.INCORRECT_LOGIN_REQUEST;
    }
    if (registeredPasswordHash == null) {
      server.getRegisteredUsers().put(name, passwordHash);
    }
    server.getModuleLogger().info("success connection from " + ioProcessor);
    onLoginSuccess(ioProcessor, name);
    return RegistrationState.SUCCESS;
  }

  private void onLoginSuccess(IOProcessor ioProcessor, String name) throws IOException {
    ioProcessor.sendMessage(converter.serialize(new LoginDTO.Success()).getBytes());
    byte[] onSuccessLogin = converter.serialize(new LoginDTO.Event(name)).getBytes();
    for (var conn : server.getConnections()) {
      try {
        conn.sendMessage(onSuccessLogin);
      } catch (IOException _) {
        server.submitExpiredConnection(conn);
      }
    }
    server.submitNewConnection(new ServerConnection(ioProcessor, name));
  }

  private void onLoginExpired() throws IOException {
    try {
      byte[] errmsg = converter.serialize(new LoginDTO.Error("login expired")).getBytes();
      ioProcessor.sendMessage(errmsg);
    } catch (UnableToSerialize e) {
      log.warn(e.getMessage(), e);
    } catch (IOException _) {
    }
    shutdownIO();
  }

  private void shutdownIO() throws IOException {
    try {
      if (ioProcessor == null) socket.close();
      else ioProcessor.close();
    } finally {
      ioProcessor = null;
    }
  }
}
