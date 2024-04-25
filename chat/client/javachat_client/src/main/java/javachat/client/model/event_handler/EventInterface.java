package javachat.client.model.event_handler;

import javachat.client.exception.IOClientException;
import javachat.client.model.Connection;
import javachat.client.model.ContextExecutor;
import org.w3c.dom.Document;

public interface EventInterface {
  void perform(Connection connection, ContextExecutor contextExecutor, MessageHandler handler, Document message) throws IOClientException;
}