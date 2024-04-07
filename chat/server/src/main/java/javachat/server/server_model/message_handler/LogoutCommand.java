package javachat.server.server_model.message_handler;

import javachat.server.exceptions.IOServerException;
import javachat.server.server_model.Connection;
import javachat.server.server_model.Server;
import javachat.server.server_model.ServerMSG;
import org.w3c.dom.Document;

public class LogoutCommand implements CommandInterface {
  @Override
  public void perform(Connection connection, Server server, MessageHandler handler, Document message) throws IOServerException {
    handler.sendMessage(connection, ServerMSG.getSuccess());
    server.submitExpiredConnection(connection);
  }
}
