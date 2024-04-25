package javachat.client.model.event_handler;

import javachat.client.exception.IOClientException;
import javachat.client.model.ChatSessionModel;
import javachat.client.model.Connection;
import org.w3c.dom.Document;

public class NewMessage implements EventInterface {
  @Override
  public void perform(Connection connection, ChatSessionModel chatSession, MessageHandler handler, Document message) throws IOClientException {

  }
}
