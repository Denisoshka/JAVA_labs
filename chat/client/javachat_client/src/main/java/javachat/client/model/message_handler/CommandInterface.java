package javachat.client.model.message_handler;

import com.almasb.fxgl.net.Server;
import javachat.client.exception.IOClientException;
import javachat.client.model.Connection;
import javachat.client.model.ContextExecutor;
import org.w3c.dom.Document;

public interface CommandInterface {
  void perform(Connection connection, ContextExecutor contextExecutor, MessageHandler handler, Document message) throws IOClientException;
}
