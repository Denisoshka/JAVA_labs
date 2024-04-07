package javachat.server.server_model.message_handler;

import javachat.server.exceptions.IOServerException;
import javachat.server.server_model.Connection;
import javachat.server.server_model.Server;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ListCommand implements CommandInterface {
  @Override
  public void perform(Connection connection, Server server, MessageHandler handler, Document message) throws IOServerException {
    final var connections = server.getConnections();
    Document doc = handler.getBuilder().newDocument();
    Element rootElement = doc.createElement(MessageHandler.SUCCESS_TAG);
    Element usersElement = doc.createElement(MessageHandler.USERS_LIST_ROOT);
    doc.appendChild(rootElement);
    rootElement.appendChild(usersElement);
    synchronized (connections) {
      for (Connection user : connections) {
        Element userElement = doc.createElement(MessageHandler.USER_TAG);
        usersElement.appendChild(userElement);
        Element nameElement = doc.createElement(MessageHandler.NAME_TAG);
        nameElement.appendChild(doc.createTextNode(user.toString()));
        userElement.appendChild(nameElement);
      }
    }
    handler.sendMessage(connection, doc);
  }
}