package server.model.message_handler;

import dto.DataDTO;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.subtypes.ListDTO;
import org.w3c.dom.Document;
import server.model.Server;
import server.model.io_processing.Connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListCommand implements CommandInterface {
  private ListDTO.ListDTOConverter converter;

  public ListCommand(Server server) {

  }

  @Override
  public void perform(Connection connection, Server server, MessageHandler handler, Document message) {
    final var connections = server.getConnections();
    final var ioprocessor = connection.getIoProcessor();
    List<DataDTO.User> users = new ArrayList<>();

    for (Connection conn : connections) {
      if (!conn.isExpired()) users.add(new DataDTO.User(conn.getConnectionName()));
    }

    try {
      try{
        ioprocessor.sendMessage(converter.serialize(new ListDTO.Success(users)));
      }catch (UnableToDeserialize e)
      ioprocessor.sendMessage(converter.serialize(new ListDTO.Error(e.getMessage())));
    } catch (UnableToSerialize ex) {
      throw new RuntimeException(ex);
    }

    try {
    } catch (UnableToDeserialize e) {

    } catch (IOException e) {
      connection.markAsExpired();
    }
  }
}