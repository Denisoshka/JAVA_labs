package server.model.message_handler;

import dto.RequestDTO;
import dto.exceptions.UnableToSerialize;
import dto.subtypes.LogoutDTO;
import server.model.Server;
import server.model.io_processing.ServerConnection;

import java.io.IOException;

public class LogoutSection implements AbstractSection {
  private final LogoutDTO.LogoutDTOConverter converter;
  private final Server server;

  public LogoutSection(LogoutDTO.LogoutDTOConverter converter, Server server) {
    this.converter = converter;
    this.server = server;
  }

  @Override
  public void perform(ServerConnection connection, RequestDTO message) {
    try {
      connection.sendMessage(converter.serialize(new LogoutDTO.Success()).getBytes());
    } catch (UnableToSerialize e) {
      Server.getLog().warn(e.getMessage());
    } catch (IOException e) {
      server.submitExpiredConnection(connection);
    }
  }
}
