package server.model.message_handler;

import server.exceptions.IOServerException;
import server.model.io_processing.Connection;
import server.model.Server;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ListCommand implements CommandInterface {
  @Override
  public void perform(Connection connection, Server server, MessageHandler handler, Document message) throws IOServerException {
    final var connections = server.getConnections();
    for (Connection user : connections) {
      Element userElement = doc.createElement(MessageHandler.USER_TAG);
      usersElement.appendChild(userElement);
      Element nameElement = doc.createElement(MessageHandler.NAME_TAG);
      nameElement.appendChild(doc.createTextNode(user.toString()));
      userElement.appendChild(nameElement);
    }
    handler.sendMessage(connection, doc);
  }
}