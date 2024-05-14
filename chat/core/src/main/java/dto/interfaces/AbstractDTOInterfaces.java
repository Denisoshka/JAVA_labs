package dto.interfaces;

import dto.RequestDTO;
import dto.DataDTO;

import java.util.List;

public interface AbstractDTOInterfaces {
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
    List<DataDTO.User> getUsers();
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

  interface DTO_SECTION {
    RequestDTO.DTO_SECTION geDTOSection();
  }
}
