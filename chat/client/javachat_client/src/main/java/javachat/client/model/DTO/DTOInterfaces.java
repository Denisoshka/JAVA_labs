package javachat.client.model.DTO;

import javachat.client.model.DTO.commands.CommandSection;
import javachat.client.model.DTO.events.Event;

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
    CommandSection.RESPONSES getStatus();
  }

  interface USERS {
    List<RequestDTO.User> getUsers();
  }

  interface EVENT_TYPE {
    Event.EVENTS getEventType();
  }

  interface RESPONSE_TYPE {
    CommandSection.RESPONSES getResponseType();
  }

  interface COMMAND_TYPE {
    CommandSection.COMMANDS getCommandType();
  }
}
