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

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

public class ChatSessionExecutor implements AbstractChatSessionExecutor, AbstractChatModuleManager, ConnectionModule {
  private final Logger defaultLogger = org.slf4j.LoggerFactory.getLogger(ChatSessionExecutor.class.getName());
  private final Logger moduleLogger = org.slf4j.LoggerFactory.getLogger("module_logger");

  private final ExecutorService actionExecutor = Executors.newSingleThreadExecutor();
  private final ExecutorService responseExecutor = Executors.newSingleThreadExecutor();
  private final ExecutorService IOExecutor = Executors.newSingleThreadExecutor();

  private final DTOConverterManager DTOConverterManager;

  public ChatModuleManager getChatModuleManager() {
    return chatModuleManager;
  }

  private final ChatModuleManager chatModuleManager;


  private final ChatSessionController chatSessionController;
  private final BlockingQueue<RequestDTO> moduleExchanger = new SynchronousQueue<>(true);

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
        defaultLogger.info(e.getMessage());
      }
    }
    this.connection = new Connection(this, hostname, port);
    IOExecutor.execute(connection);
  }

  public void executeAction(Runnable task) {
    actionExecutor.execute(task);
  }

  public void executeResponse(Runnable task) {
    responseExecutor.execute(task);
  }

  @Override
  public IOProcessor getIOProcessor() {
    return connection.getIoProcessor();
  }

  @Override
  public Logger getModuleLogger() {
    return moduleLogger;
  }

  public Logger getDefaultLogger() {
    return defaultLogger;
  }


  public ChatSessionController getChatSessionController() {
    return chatSessionController;
  }

  @Override
  public BlockingQueue<RequestDTO> getModuleExchanger() {
    return moduleExchanger;
  }

  public DTOConverterManager getDTOConverterManager() {
    return DTOConverterManager;
  }

  @Override
  public ChatModule getChatModule(RequestDTO.DTO_SECTION moduleSection) {
    return chatModuleManager.getChatModule(moduleSection);
  }
}
