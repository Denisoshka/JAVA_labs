package server.model.server_sections;

import dto.RequestDTO;
import dto.exceptions.UnableToSerialize;
import dto.subtypes.LogoutDTO;
import org.w3c.dom.Document;
import server.model.Server;
import server.model.io_processing.ServerConnection;
import server.model.server_sections.interfaces.AbstractSection;

import java.io.IOException;

public class LogoutSection implements AbstractSection {
  private final LogoutDTO.LogoutDTOConverter converter;
  private final Server server;

  public LogoutSection(LogoutDTO.LogoutDTOConverter converter, Server server) {
    this.converter = converter;
    this.server = server;
  }

  @Override
  public void perform(ServerConnection connection, Document message, RequestDTO.DTO_TYPE dtoType, RequestDTO.DTO_SECTION section) throws IOException {
    try {
      connection.sendMessage(converter.serialize(new LogoutDTO.Success()).getBytes());
      server.submitExpiredConnection(connection);
    } catch (UnableToSerialize e) {
      Server.getLog().warn(e.getMessage());
    }
  }
}
