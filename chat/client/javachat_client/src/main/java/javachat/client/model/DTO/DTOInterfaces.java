package javachat.client.model.DTO;

import javachat.client.model.DTO.commands.COMMAND_SECTION;

import java.util.List;

public interface DTOInterfaces {
  interface NAME_ATTRIBUTE {
    String getNameAttribute();
  }

  interface HOSTNAME {
    String getHostname();
  }

  interface PORT {
    int getPort();
  }

  interface PASSWORD {
    String getPassword();
  }

  interface MESSAGE {
    String getMessage();
  }

  interface NAME {
    String getName();
  }

  interface FROM {
    String getFrom();
  }

  interface STATUS {
    COMMAND_SECTION.RESPONSE_STATUS getStatus();
  }

  interface USERS {
    List<RequestDTO.User> getUsers();
  }

  interface TYPE {
    String getType();
  }
}
