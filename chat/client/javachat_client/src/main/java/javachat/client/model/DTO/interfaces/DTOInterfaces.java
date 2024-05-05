package javachat.client.model.DTO.interfaces;

import javachat.client.model.DTO.RequestDTO;
import javachat.client.model.DTO.XyiDTO;

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

  interface USERS {
    List<XyiDTO.User> getUsers();
  }

  interface DTO_TYPE {
    RequestDTO.DTO_TYPE getDTOType();
  }

  interface EVENT_TYPE {
    RequestDTO.BaseEvent.EVENT_TYPE getEventType();
  }

  interface RESPONSE_TYPE {
    RequestDTO.BaseResponse.RESPONSE_TYPE getResponseType();
  }

  interface COMMAND_TYPE {
    RequestDTO.BaseCommand.COMMAND_TYPE getCommandType();
  }
}
