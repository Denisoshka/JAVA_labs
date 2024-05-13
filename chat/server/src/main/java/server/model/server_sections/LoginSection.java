package server.model.server_sections;

import dto.DTOConverterManager;
import dto.RequestDTO;
import dto.exceptions.UnableToSerialize;
import dto.subtypes.LoginDTO;
import io_processing.IOProcessor;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Node;
import server.model.RegistrationState;
import server.model.Server;
import server.model.io_processing.ServerConnection;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class LoginSection implements Runnable {
  private static final long MAX_CONNECTION_TIME = 60_000;
  private final ConcurrentHashMap<String, Integer> registeredUsers;
  private final LoginDTO.LoginDTOConverter converter;
  private final DTOConverterManager manager;
  private final Server server;

  private IOProcessor ioProcessor = null;
  private volatile long now;
  private volatile long start;

  public LoginSection(Server server, Socket socket) {
    this.manager = server.getConverterManager();
    this.converter = (LoginDTO.LoginDTOConverter) manager.getConverter(RequestDTO.DTO_SECTION.LOGIN);
    this.registeredUsers = server.getRegisteredUsers();
    this.server = server;
    try {
      this.ioProcessor = new IOProcessor(socket);
    } catch (IOException _) {
    }
  }

  @Override
  public void run() {
    if (ioProcessor == null) return;
    try {
      for (start = System.currentTimeMillis(), now = start;
           Thread.currentThread().isInterrupted() && now - start < MAX_CONNECTION_TIME;
           now = System.currentTimeMillis()) {
        Node root;
        try {
          root = manager.getXMLTree(ioProcessor.receiveMessage());
          RequestDTO.DTO_SECTION dtoSection = manager.getDTOSection(root);
          RequestDTO.DTO_TYPE dtoType = manager.getDTOType(root);
          if (handleLoginRequest(dtoSection, dtoType, root) == RegistrationState.SUCCESS) break;
        } catch (UnableToSerialize _) {
        }
      }
    } catch (IOException _) {
      try {
        ioProcessor.close();
        ioProcessor = null;
      } catch (IOException _) {
      }
    } catch (Exception e) {
      log.warn(e.getMessage(), e);
    }
  }

  private RegistrationState handleLoginRequest(RequestDTO.DTO_SECTION dtoSection, RequestDTO.DTO_TYPE dtoType, Node root) throws IOException {
    if (dtoSection != RequestDTO.DTO_SECTION.LOGIN || dtoType != RequestDTO.DTO_TYPE.COMMAND) {
      ioProcessor.sendMessage(converter.serialize(new LoginDTO.Error(
              STR."excepted name=login actual: \{dtoType}\n excepted type=command actual: \{dtoSection}")).getBytes()
      );
      return RegistrationState.INCORRECT_LOGIN_REQUEST;
    }
    LoginDTO.Command command = (LoginDTO.Command) converter.deserialize(root);
    String name = command.getName();
    String password = command.getPassword();
    Integer registeredPassword = registeredUsers.get(name);
    if (registeredPassword != null && password.hashCode() != registeredPassword) {
      ioProcessor.sendMessage(converter.serialize(new LoginDTO.Error("incorrect password")).getBytes());
      return RegistrationState.INCORRECT_LOGIN_REQUEST;
    }
    if (registeredPassword == null) {
      registeredUsers.put(name, password.hashCode());
    }

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
}
