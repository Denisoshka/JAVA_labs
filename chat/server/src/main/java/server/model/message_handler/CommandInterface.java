package server.model.message_handler;

import dto.RequestDTO;
import server.exceptions.IOServerException;
import server.model.Server;
import server.model.io_processing.Connection;

public interface CommandInterface {
  void perform(Connection connection, Server server, RequestDTO dto) throws IOServerException;
}
