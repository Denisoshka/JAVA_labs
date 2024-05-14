package client.model.main_context.interfaces;

import org.slf4j.Logger;
import org.w3c.dom.Document;

import java.util.concurrent.BlockingQueue;

public interface AbstractChatSessionExecutor {
  Logger getModuleLogger();

  Logger getDefaultLogger();

  BlockingQueue<Document> getModuleExchanger();
}