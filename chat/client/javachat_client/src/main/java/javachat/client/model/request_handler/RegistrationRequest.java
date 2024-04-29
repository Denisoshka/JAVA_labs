package javachat.client.model.request_handler;

import javachat.client.exception.IOClientException;
import javachat.client.model.ChatSessionExecutor;
import javachat.client.model.Connection;
import org.w3c.dom.Document;

public class RegistrationRequest implements RequestInterface{

  @Override
  public void perform(Connection connection, ChatSessionExecutor chatSession, IOHandler handler, String message) {

  }
}
