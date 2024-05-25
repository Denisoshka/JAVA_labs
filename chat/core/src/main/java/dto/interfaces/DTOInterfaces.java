package dto.interfaces;

import dto.DataDTO;
import dto.RequestDTO;
import dto.subtypes.FileDTO;

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
    List<DataDTO.User> getUsers();
  }

  interface FILES {
    List<FileDTO.FileEntity> getFiles();
  }

  interface DTO_TYPE {
    RequestDTO.DTO_TYPE getDTOType();
  }

  interface EVENT_TYPE {
    RequestDTO.EVENT_TYPE getEventType();
  }

  interface RESPONSE_TYPE {
    RequestDTO.RESPONSE_TYPE getResponseType();
  }

  interface COMMAND_TYPE {
    RequestDTO.COMMAND_TYPE getCommandType();
  }

  interface MIME_TYPE {
    String getMimeType();
  }

  interface ENCODING {
    String getEncoding();
  }

  interface CONTENT {
    byte[] getContent();
  }

  interface ID {
    Long getId();
  }

  interface SIZE {
    long getSize();
  }

  interface DTO_SECTION {
    RequestDTO.DTO_SECTION geDTOSection();
  }

  interface REQUEST_DTO {
    RequestDTO.DTO_TYPE getDTOType();

    RequestDTO.DTO_SECTION getDTOSection();
  }

  interface COMMAND_DTO extends REQUEST_DTO, NAME_ATTRIBUTE {
    String getNameAttribute();

    @Override
    default RequestDTO.DTO_TYPE getDTOType() {
      return RequestDTO.DTO_TYPE.COMMAND;
    }

    RequestDTO.COMMAND_TYPE getCommandType();
  }

  interface EVENT_DTO extends REQUEST_DTO, NAME_ATTRIBUTE {
    RequestDTO.EVENT_TYPE getEventType();

    @Override
    default RequestDTO.DTO_TYPE getDTOType() {
      return RequestDTO.DTO_TYPE.EVENT;
    }
  }

  interface RESPONSE_DTO extends REQUEST_DTO {
    RequestDTO.RESPONSE_TYPE getResponseType();
  }

  interface SUCCESS_RESPONSE_DTO extends RESPONSE_DTO {
    @Override
    default RequestDTO.DTO_TYPE getDTOType() {
      return RequestDTO.DTO_TYPE.RESPONSE;
    }

    @Override
    default RequestDTO.RESPONSE_TYPE getResponseType() {
      return RequestDTO.RESPONSE_TYPE.SUCCESS;
    }
  }

  interface ERROR_RESPONSE_DTO extends RESPONSE_DTO, MESSAGE {
    @Override
    default RequestDTO.DTO_TYPE getDTOType() {
      return RequestDTO.DTO_TYPE.RESPONSE;
    }

    @Override
    default RequestDTO.RESPONSE_TYPE getResponseType() {
      return RequestDTO.RESPONSE_TYPE.ERROR;
    }
  }
}
