package javachat.client.model.main_context;

import javachat.client.model.DTO.RequestDTO;
import org.slf4j.Logger;

import java.util.concurrent.BlockingQueue;

public interface ChatSessionExecutorInterface {
  Logger getModuleLogger();

  Logger getDefaultLogger();

  BlockingQueue<RequestDTO> getModuleExchanger();
}
