package client.model.main_context.interfaces;

import dto.RequestDTO;
import org.slf4j.Logger;

import java.util.concurrent.BlockingQueue;

public interface AbstractChatSessionExecutor {
  Logger getModuleLogger();

  Logger getDefaultLogger();

  BlockingQueue<RequestDTO> getModuleExchanger();
}