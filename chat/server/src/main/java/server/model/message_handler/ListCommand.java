package server.model.message_handler;

import dto.DTOConverterManager;
import dto.DataDTO;
import dto.RequestDTO;
import dto.exceptions.UnableToSerialize;
import dto.subtypes.ListDTO;
import server.model.Server;
import server.model.io_processing.Connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListCommand implements CommandInterface {
  private ListDTO.ListAbstractDTOConverter converter;
  private Server server;

  public ListCommand(Server server, DTOConverterManager converterManager) {
    this.server = server;
  }

  @Override
  public void perform(Connection connection, Server server, RequestDTO message) {
    final var connections = server.getConnections();
    final var ioprocessor = connection.getIoProcessor();
    List<DataDTO.User> users = new ArrayList<>();

    for (Connection conn : connections) {
      if (!conn.isExpired()) users.add(new DataDTO.User(conn.getConnectionName()));
    }

    try {
      try {
        ioprocessor.sendMessage(converter.serialize(new ListDTO.Success(users)));
      } catch (UnableToSerialize e) {
        ioprocessor.sendMessage(converter.serialize(new ListDTO.Error(e.getMessage())));
      }
    } catch (IOException ex) {
      connection.markAsExpired();
    }
  }
}