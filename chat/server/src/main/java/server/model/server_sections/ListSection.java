package server.model.server_sections;

import dto.RequestDTO;
import dto.exceptions.UnableToSerialize;
import dto.subtypes.list.ListDTOConverter;
import dto.subtypes.list.ListError;
import dto.subtypes.list.ListSuccess;
import dto.subtypes.other.User;
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
  private final ListDTOConverter converter;
  private final Server server;

  public ListSection(ListDTOConverter converter, Server server) {
    this.converter = converter;
    this.server = server;
  }

  @Override
  public void perform(ServerConnection connection, Document message, RequestDTO.DTO_TYPE type, RequestDTO.DTO_SECTION section) {
    if (type != RequestDTO.DTO_TYPE.COMMAND) {
      var errmsg = STR."not support \{type.name()}";
      log.info(errmsg);
      try {
        connection.sendMessage(converter.serialize(new ListError(errmsg)).getBytes());
      } catch (IOException e) {
        server.submitExpiredConnection(connection);
      }
    }

    final var connections = server.getConnections();
    final var usersDao = server.getChatUsersDAO();

    List<User> users = new ArrayList<>();
    for (ServerConnection conn : connections) {
      if (conn.isExpired()) continue;

      var res = usersDao.findAccountByUserName(conn.getConnectionName());
      users.add(new User(conn.getConnectionName(), res.getAvatar(), res.getAvatarMimeType()));
    }

    try {
      connection.sendMessage(converter.serialize(new ListSuccess(users)).getBytes());
    } catch (UnableToSerialize e1) {
      try {
        connection.sendMessage(converter.serialize(new ListError(e1.getMessage())).getBytes());
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