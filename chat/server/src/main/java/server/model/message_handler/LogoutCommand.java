package server.model.message_handler;

import server.exceptions.IOServerException;
import server.model.io_processing.Connection;
import server.model.Server;
import org.w3c.dom.Document;

public class LogoutCommand implements CommandInterface {
  @Override
  public void perform(Connection connection, Server server, MessageHandler handler, Document message) throws IOServerException {
    handler.sendMessage(connection, ServerMSG.getSuccess());
    server.submitExpiredConnection(connection);
  }
}
