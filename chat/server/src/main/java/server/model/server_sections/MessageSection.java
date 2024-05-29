package server.model.server_sections;

import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import server.model.Server;
import server.model.connection_section.ServerConnection;
import server.model.server_sections.interfaces.AbstractSection;

import java.io.IOException;

public class MessageSection implements AbstractSection {
  private static final Logger log = LoggerFactory.getLogger(MessageSection.class);
  private final MessageDTO.MessageDTOConverter converter;
  private final Server server;

  public MessageSection(MessageDTO.MessageDTOConverter converter, Server server) {
    this.converter = converter;
    this.server = server;
  }

  @Override
  public void perform(ServerConnection connection, Document root, RequestDTO.DTO_TYPE type, RequestDTO.DTO_SECTION section) throws IOException {
    try {
      MessageDTO.Command messageDTO;
      try {
        messageDTO = (MessageDTO.Command) converter.deserialize(root);
        connection.sendMessage(converter.serialize(new MessageDTO.Success()).getBytes());
      } catch (UnableToDeserialize e) {
        log.info(e.getMessage(), e);
        return;
      } catch (IOException e) {
        server.submitExpiredConnection(connection);
        log.error(e.getMessage(), e);
        return;
      }

      byte[] msg = converter.serialize(new MessageDTO.Event(
              connection.getConnectionName(),
              messageDTO.getMessage()
      )).getBytes();
      for (var conn : server.getConnections()) {
        if (conn.isExpired()) {
          continue;
        }
        try {
          conn.sendMessage(msg);
        } catch (IOException e) {
          server.submitExpiredConnection(connection);
        }
      }
    } catch (UnableToSerialize e) {
      log.warn(e.getMessage());
    }
  }
}
