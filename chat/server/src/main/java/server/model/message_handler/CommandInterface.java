package server.model.message_handler;

import server.exceptions.IOServerException;
import server.model.io_processing.Connection;
import server.model.Server;

import org.w3c.dom.Document;

public interface CommandInterface {
  void perform(Connection connection, Server server, MessageHandler handler, Document message) throws IOServerException;
}
