package javachat.server.server_model.message_handler;

import javachat.server.exceptions.IOServerException;
import javachat.server.server_model.Connection;
import javachat.server.server_model.Server;
import javachat.server.server_model.ServerMSG;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class MessageCommand implements CommandInterface {
  /*
  <command name="message">
    <message>MESSAGE</message>
  </command>
  */
  @Override
  public void perform(Connection connection, Server server, MessageHandler handler, Document message) throws IOServerException {
    NodeList purpose = message.getDocumentElement().getElementsByTagName(MessageHandler.COMMAND_MESSAGE_TAG);
    if (purpose.getLength() < 1) {
      handler.sendMessage(connection, ServerMSG.getError("no message"));
    }
    handler.sendMessage(connection, ServerMSG.getSuccess());
    handler.sendBroadcastMessage(connection, ServerMSG.getMessage(server.getSessionName(), purpose.item(0).getTextContent()));
  }
}
