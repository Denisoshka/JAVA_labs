package server.model.message_handler;

import dto.RequestDTO;
import server.exceptions.IOServerException;
import server.model.Server;
import server.model.io_processing.Connection;

public class LogoutSection implements CommandInterface {



  @Override
  public void perform(Connection connection, Server server, RequestDTO message) throws IOServerException {
    connection.sendMessage();
    handler.sendMessage(connection, ServerMSG.getSuccess());
    server.submitExpiredConnection(connection);
  }
}
