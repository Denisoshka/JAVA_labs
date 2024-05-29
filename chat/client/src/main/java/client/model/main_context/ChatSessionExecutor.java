package client.model.main_context;

import client.facade.ChatSessionController;
import client.model.chat_modules.ChatModuleManager;
import client.model.chat_modules.interfaces.AbstractChatModuleManager;
import client.model.chat_modules.interfaces.ChatModule;
import client.model.io_processing.Connection;
import client.model.main_context.interfaces.AbstractChatSessionExecutor;
import client.model.main_context.interfaces.ConnectionModule;
import dto.DTOConverterManager;
import dto.RequestDTO;
import io_processing.IOProcessor;
import org.slf4j.Logger;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

public class ChatSessionExecutor implements AbstractChatSessionExecutor, AbstractChatModuleManager, ConnectionModule, AutoCloseable {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(ChatSessionExecutor.class);

  private final ExecutorService chatModuleExecutor = Executors.newSingleThreadExecutor();
  private final ExecutorService ioExecutor = Executors.newSingleThreadExecutor();

  private final DTOConverterManager DTOConverterManager;
  private final ChatModuleManager chatModuleManager;
  private final ChatSessionController chatSessionController;
  private final BlockingQueue<Document> moduleExchanger = new SynchronousQueue<>(true);

  private Connection connection;

  public ChatSessionExecutor(ChatSessionController chatSessionController) throws IOException {
    this.chatSessionController = chatSessionController;
    Properties properties = new Properties();
    try (var input = getClass().getResourceAsStream("xml_converter_manager.properties")) {
      properties.load(input);
      this.DTOConverterManager = new DTOConverterManager(properties);
    }
    this.chatModuleManager = new ChatModuleManager(null, this);
  }

  @Override
  public void introduceConnection(String hostname, int port) throws IOException {
    if (connection != null) {
      try {
        connection.close();
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
    }
    this.connection = new Connection(this, hostname, port);
    ioExecutor.execute(connection);
  }

  @Override
  public void shutdownConnection() throws IOException {
    chatSessionController.onConnectResponse(ConnectionModule.ConnectionState.DISCONNECTED);
    try {
      connection.close();
    } finally {
      connection = null;
    }
  }


  @Override
  public IOProcessor getIOProcessor() {
    return connection.getIoProcessor();
  }


  @Override
  public BlockingQueue<Document> getModuleExchanger() {
    return moduleExchanger;
  }


  @Override
  public ChatModule getChatModule(RequestDTO.DTO_SECTION moduleSection) {
    return chatModuleManager.getChatModule(moduleSection);
  }


  public void executeModuleAction(Runnable task) {
    chatModuleExecutor.execute(task);
  }


  public boolean isConnected() {
    return connection != null && !connection.isClosed();
  }


  public ChatSessionController getChatSessionController() {
    return chatSessionController;
  }


  public DTOConverterManager getDTOConverterManager() {
    return DTOConverterManager;
  }


  public ChatModuleManager getChatModuleManager() {
    return chatModuleManager;
  }


  public ExecutorService getChatModuleExecutor() {
    return chatModuleExecutor;
  }

  @Override
  public void close() {
    ioExecutor.shutdownNow();
    chatModuleExecutor.shutdownNow();
    try {
      if (connection != null) connection.close();
    } catch (IOException _) {
    }
  }
}
