package javachat.client.model.request_handler.requests;

import javachat.client.exception.IOClientException;
import javachat.client.model.ChatSessionExecutor;
import javachat.client.model.Connection;
import javachat.client.model.request_handler.IOHandler;
import javachat.client.model.request_handler.RequestInterface;
import org.w3c.dom.Document;

public class UnknownRequest implements RequestInterface {
  @Override
  public void perform(Connection connection, ChatSessionExecutor chatSession, IOHandler handler, Document message) throws IOClientException {

  }
}
