package server.model.server_sections;

import dto.DataDTO;
import dto.RequestDTO;
import dto.exceptions.UnableToSerialize;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import server.model.Server;
import server.model.connection_section.ServerConnection;
import server.model.server_sections.interfaces.AbstractSection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListSection implements AbstractSection {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(ListSection.class);
  private final ListDTO.ListDTOConverter converter;
  private final Server server;

  public ListSection(ListDTO.ListDTOConverter converter, Server server) {
    this.converter = converter;
    this.server = server;
  }

  @Override
  public void perform(ServerConnection connection, Document message, RequestDTO.DTO_TYPE type, RequestDTO.DTO_SECTION section) {
    if (type != RequestDTO.DTO_TYPE.COMMAND) {
      var errmsg = STR."not support \{type.name()}";
      server.getModuleLogger().info(errmsg);
      try {
        connection.sendMessage(converter.serialize(new ListDTO.Error(errmsg)).getBytes());
      } catch (IOException e) {
        server.submitExpiredConnection(connection);
      }
    }

    final var connections = server.getConnections();
    List<DataDTO.User> users = new ArrayList<>();
    for (ServerConnection conn : connections) {
      if (!conn.isExpired()) users.add(new DataDTO.User(conn.getConnectionName()));
    }

    try {
      connection.sendMessage(converter.serialize(new ListDTO.Success(users)).getBytes());
      server.getModuleLogger().info("ListDTO.Success");
    } catch (UnableToSerialize e1) {
      try {
        connection.sendMessage(converter.serialize(new ListDTO.Error(e1.getMessage())).getBytes());
      } catch (UnableToSerialize e2) {
//        todo handle this
        log.warn(e1.getMessage(), e1);
      } catch (IOException e2) {
        server.submitExpiredConnection(connection);
      }
    } catch (IOException e1) {
      server.submitExpiredConnection(connection);
    }
  }
}