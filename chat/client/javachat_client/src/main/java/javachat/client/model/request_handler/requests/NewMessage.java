package javachat.client.model.request_handler.requests;

import javachat.client.model.ChatSessionExecutor;
import javachat.client.model.Connection;
import javachat.client.model.request_handler.IOHandler;
import javachat.client.model.request_handler.RequestInterface;

public class NewMessage implements RequestInterface {
  @Override
  public void perform(Connection connection, ChatSessionExecutor chatSession, IOHandler handler, String message) {

  }
}
