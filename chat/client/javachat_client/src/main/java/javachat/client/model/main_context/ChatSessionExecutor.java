package javachat.client.model.main_context;

import javachat.client.facade.ChatSessionController;
import javachat.client.model.DTO.XMLConverterManager;
import javachat.client.model.DTO.interfaces.XMLConverterManageInterface;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatSessionExecutor implements ConnectionIntroducer {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(ChatSessionExecutor.class);
  private final ExecutorService inputExecutor = Executors.newSingleThreadExecutor();
  private final ExecutorService outputExecutor = Executors.newSingleThreadExecutor();

  private final XMLConverterManageInterface xmlConverterManager;
  private final ChatSessionController controller;

  private Connection connection;

  public ChatSessionExecutor(ChatSessionController controller) throws IOException {
    this.controller = controller;
    Properties properties = new Properties();
    try (var input = getClass().getResourceAsStream("xml_converter_manager.properties")) {
      properties.load(input);
      this.xmlConverterManager = new XMLConverterManager(properties);
    }
  }

  @Override
  public void setConnection(Connection connection) {
    outputExecutor.execute(connection);
    this.connection = connection;
  }


}
