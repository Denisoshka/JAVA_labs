package javachat.client.model.main_context;

import javachat.client.model.dto.RequestDTO;
import org.slf4j.Logger;

import java.util.concurrent.BlockingQueue;

public interface AbstractChatSessionExecutor {
  Logger getModuleLogger();

  Logger getDefaultLogger();

  BlockingQueue<RequestDTO> getModuleExchanger();
}