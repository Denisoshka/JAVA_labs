package server.model.server_sections;

import dto.DataDTO;
import dto.exceptions.UnableToSerialize;
import dto.subtypes.ListDTO;
import org.w3c.dom.Node;
import server.model.Server;
import server.model.io_processing.ServerConnection;
import server.model.server_sections.interfaces.AbstractSection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListSection implements AbstractSection {
  private final ListDTO.ListDTOConverter converter;
  private final Server server;

  public ListSection(ListDTO.ListDTOConverter converter, Server server) {
    this.converter = converter;
    this.server = server;
  }

  @Override
  public void perform(ServerConnection connection, Node message) throws IOException {
    final var connections = server.getConnections();
    List<DataDTO.User> users = new ArrayList<>();
    for (ServerConnection conn : connections) {
      if (!conn.isExpired()) users.add(new DataDTO.User(conn.getConnectionName()));
    }

    try {
      connection.sendMessage(converter.serialize(new ListDTO.Success(users)).getBytes());
    } catch (UnableToSerialize e1) {
      try {
        connection.sendMessage(converter.serialize(new ListDTO.Error(e1.getMessage())).getBytes());
      } catch (UnableToSerialize e2) {
//        todo handle this
        Server.getLog().warn(e1.getMessage(), e1);
      }
    }
  }
}