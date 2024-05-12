package javachat.client.model.main_context;

import javachat.client.facade.ChatSessionController;
import javachat.client.model.chat_modules.command.ChatModule;
import javachat.client.model.dto.DTOConverterManager;
import javachat.client.model.dto.RequestDTO;
import javachat.client.model.io_processing.Connection;
import javachat.client.model.io_processing.IOProcessor;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

public class ChatSessionExecutor implements ChatSessionExecutorInterface, ConnectionModule {
  private final Logger defaultLogger = org.slf4j.LoggerFactory.getLogger(ChatSessionExecutor.class.getName());
  private final Logger moduleLogger = org.slf4j.LoggerFactory.getLogger("module_logger");

  private final ExecutorService actionExecutor = Executors.newSingleThreadExecutor();
  private final ExecutorService responseExecutor = Executors.newSingleThreadExecutor();
  private final ExecutorService IOExecutor = Executors.newSingleThreadExecutor();


  private final DTOConverterManager XMLDTOConverterManager;

  private final ChatSessionController controller;
  private final BlockingQueue<RequestDTO> moduleExchanger = new SynchronousQueue<>(true);

  private Connection connection;

  public ChatSessionExecutor(ChatSessionController controller) throws IOException {
    this.controller = controller;
    Properties properties = new Properties();
    try (var input = getClass().getResourceAsStream("xml_converter_manager.properties")) {
      properties.load(input);
      this.XMLDTOConverterManager = new DTOConverterManager(properties);
    }
  }

  @Override
  public IOProcessor introduceConnection(String hostname, int port) throws IOException {
    this.connection = new Connection(hostname, port);
    IOExecutor.execute(connection);
    return connection.getIoProcessor();
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


  public ChatSessionController getController() {
    return controller;
  }

  @Override
  public BlockingQueue<RequestDTO> getModuleExchanger() {
    return moduleExchanger;
  }

  @Override
  public ChatModule getChatModule() {
    return null;
  }

  public DTOConverterManager getXMLDTOConverterManager() {
    return XMLDTOConverterManager;
  }
}
