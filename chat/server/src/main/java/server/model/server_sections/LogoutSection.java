package server.model.server_sections;

import dto.RequestDTO;
import dto.exceptions.UnableToSerialize;
import dto.subtypes.logout.LogoutDTOConverter;
import dto.subtypes.logout.LogoutEvent;
import dto.subtypes.logout.LogoutSuccess;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import server.model.Server;
import server.model.connection_section.ServerConnection;
import server.model.server_sections.interfaces.AbstractSection;

import java.io.IOException;

public class LogoutSection implements AbstractSection {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(LogoutSection.class);
  private final LogoutDTOConverter converter;
  private final Server server;

  public LogoutSection(LogoutDTOConverter converter, Server server) {
    this.converter = converter;
    this.server = server;
  }

  @Override
  public void perform(ServerConnection connection, Document message, RequestDTO.DTO_TYPE type, RequestDTO.DTO_SECTION section) {
    try {
      connection.sendMessage(converter.serialize(new LogoutSuccess()).getBytes());
      byte[] msg = converter.serialize(new LogoutEvent(connection.getConnectionName())).getBytes();
      for (var con : server.getConnections()) {
        con.sendMessage(msg);
      }
    } catch (UnableToSerialize e) {
      log.warn(e.getMessage());
    } catch (IOException _) {
    } finally {
      server.submitExpiredConnection(connection);
    }
  }
}
