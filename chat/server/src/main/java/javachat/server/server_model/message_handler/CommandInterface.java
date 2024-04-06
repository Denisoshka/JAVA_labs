package javachat.server.server_model.message_handler;

import javachat.server.exceptions.IOServerException;
import javachat.server.server_model.Connection;
import javachat.server.server_model.Server;

import org.w3c.dom.Document;

public interface CommandInterface {
  void perform(Connection connection, Server server, MessageHandler handler, Document message) throws IOServerException;
}
