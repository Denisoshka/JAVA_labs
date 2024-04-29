package javachat.client.model.DTO.commands;

import javachat.client.model.DTO.DTOInterfaces;
import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.*;
import java.util.Objects;

public enum LogoutSection {
  ;

  @XmlRootElement(name = "command")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Command extends COMMAND_SECTION.Command {
    public Command() {
      super(COMMAND_SECTION.LOGOUT.getType());
    }
  }

  @EqualsAndHashCode(callSuper = true)
  @XmlSeeAlso({SuccessResponse.class, ErrorResponse.class})
  public static class Response extends COMMAND_SECTION.BaseResponse {
    public Response() {
      super(COMMAND_SECTION.RESPONSE_STATUS.UNKNOWN);
    }

    public Response(COMMAND_SECTION.RESPONSE_STATUS status) {
      super(status);
    }
  }

  @XmlRootElement(name = "success")
  public static class SuccessResponse extends Response {
    public SuccessResponse() {
      super(COMMAND_SECTION.RESPONSE_STATUS.SUCCESS);
    }
  }

  @XmlRootElement(name = "error")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class ErrorResponse extends Response implements DTOInterfaces.MESSAGE {
    @XmlElement(name = "message")
    private String message;

    public ErrorResponse() {
      super(COMMAND_SECTION.RESPONSE_STATUS.ERROR);
    }

    public ErrorResponse(String message) {
      this();
      this.message = message;
    }

    @Override
    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof ErrorResponse that)) return false;
      return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
      return Objects.hash(message);
    }
  }
}
