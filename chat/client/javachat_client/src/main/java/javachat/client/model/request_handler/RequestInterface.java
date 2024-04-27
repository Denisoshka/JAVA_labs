package javachat.client.model.request_handler;

import javachat.client.model.ChatSessionExecutor;
import javachat.client.model.Connection;

public interface RequestInterface {
  void perform(Connection connection, ChatSessionExecutor chatSession, IOHandler handler, String message);
}
