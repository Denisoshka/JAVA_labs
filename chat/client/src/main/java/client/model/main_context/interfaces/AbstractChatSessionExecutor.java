package client.model.main_context.interfaces;

import org.w3c.dom.Document;

import java.util.concurrent.BlockingQueue;

public interface AbstractChatSessionExecutor {
  BlockingQueue<Document> getModuleExchanger();
}