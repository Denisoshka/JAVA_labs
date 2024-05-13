package server.model.server_sections;

import dto.exceptions.UnableToSerialize;
import dto.subtypes.MessageDTO;
import org.w3c.dom.Node;
import server.model.Server;
import server.model.io_processing.ServerConnection;
import server.model.server_sections.interfaces.AbstractSection;

import java.io.IOException;

public class MessageSection implements AbstractSection {
  private final MessageDTO.MessageDTOConverter converter;
  private final Server server;

  public MessageSection(MessageDTO.MessageDTOConverter converter, Server server) {
    this.converter = converter;
    this.server = server;
  }

  @Override
  public void perform(ServerConnection connection, Node dto) throws IOException {
    try {
      MessageDTO.Command messageDTO = (MessageDTO.Command) converter.deserialize(dto);
      byte[] msg = converter.serialize(new MessageDTO.Event(
              connection.getConnectionName(),
              messageDTO.getMessage()
      )).getBytes();
      for (var conn : server.getConnections()) {
        if (connection.equals(conn) || conn.isExpired()) {
          continue;
        }
        try {
          conn.sendMessage(msg);
        } catch (IOException e) {
          server.submitExpiredConnection(connection);
        }
      }
    } catch (UnableToSerialize e) {
//      todo maybe make server shutdown when this occurs
      Server.getLog().warn(e.getMessage());
    }
  }
}
