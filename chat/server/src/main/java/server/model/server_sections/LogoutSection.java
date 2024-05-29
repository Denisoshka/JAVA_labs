package server.model.server_sections;

import dto.RequestDTO;
import dto.exceptions.UnableToSerialize;
import org.w3c.dom.Document;
import server.model.Server;
import server.model.connection_section.ServerConnection;
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
  public void perform(ServerConnection connection, Document message, RequestDTO.DTO_TYPE type, RequestDTO.DTO_SECTION section) {
    try {
      connection.sendMessage(converter.serialize(new LogoutDTO.Success()).getBytes());
      byte[] msg = converter.serialize(new LogoutDTO.Event(connection.getConnectionName())).getBytes();
      for (var con : server.getConnections()){
        con.sendMessage(msg);
      }
    } catch (UnableToSerialize e) {
      server.getModuleLogger().warn(e.getMessage());
    } catch (IOException _) {
    } finally {
      server.submitExpiredConnection(connection);
    }
  }
}
