package server.model.message_handler;

import server.exceptions.IOServerException;
import server.model.io_processing.Connection;
import server.model.Server;
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
