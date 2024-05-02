package javachat.client.model.DTO;

import javachat.client.model.DTO.commands.CommandSection;

import java.util.List;

public interface DTOInterfaces {
  interface NAME_ATTRIBUTE {
    String getNameattribute();
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
    CommandSection.RESPONSE_STATUS getStatus();
  }

  interface USERS {
    List<RequestDTO.User> getUsers();
  }

  interface TYPE {
    String getType();
  }
}
