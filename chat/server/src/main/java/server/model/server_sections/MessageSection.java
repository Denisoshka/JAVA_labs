package server.model.server_sections;

import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.subtypes.message.MessageCommand;
import dto.subtypes.message.MessageDTOConverter;
import dto.subtypes.message.MessageEvent;
import dto.subtypes.message.MessageSuccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import server.model.Server;
import server.model.connection_section.ServerConnection;
import server.model.server_sections.interfaces.AbstractSection;

import java.io.IOException;

public class MessageSection implements AbstractSection {
  private static final Logger log = LoggerFactory.getLogger(MessageSection.class);
  private final MessageDTOConverter converter;
  private final Server server;

  public MessageSection(MessageDTOConverter converter, Server server) {
    this.converter = converter;
    this.server = server;
  }

  @Override
  public void perform(ServerConnection connection, Document root, RequestDTO.DTO_TYPE type, RequestDTO.DTO_SECTION section) throws IOException {
    try {
      MessageCommand messageDTO;
      try {
        messageDTO = (MessageCommand) converter.deserialize(root);
        connection.sendMessage(converter.serialize(new MessageSuccess()).getBytes());
      } catch (UnableToDeserialize e) {
        log.info(e.getMessage(), e);
        return;
      } catch (IOException e) {
        server.submitExpiredConnection(connection);
        log.error(e.getMessage(), e);
        return;
      }

      byte[] msg = converter.serialize(new MessageEvent(
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
