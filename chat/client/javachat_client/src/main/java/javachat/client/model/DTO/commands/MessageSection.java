package javachat.client.model.DTO.commands;

import javachat.client.model.DTO.DTOInterfaces;
import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.*;
import java.util.Objects;

import static javachat.client.model.DTO.commands.COMMAND_SECTION.MESSAGE;


public enum MessageSection {
  ;

  @XmlRootElement(name = "command")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static final class Command extends COMMAND_SECTION.Command implements DTOInterfaces.MESSAGE {
    @XmlElement(name = "message", required = true)
    private String message;

    public Command() {
      super(MESSAGE.getType());
    }

    public Command(String message) {
      this();
      this.message = message;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Command message1)) return false;
      if (!super.equals(o)) return false;
      return Objects.equals(message, message1.message);
    }

    @Override
    public int hashCode() {
      return Objects.hash(super.hashCode(), message);
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
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
  @EqualsAndHashCode(callSuper = true)
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class SuccessResponse extends Response {
    public SuccessResponse() {
      super(COMMAND_SECTION.RESPONSE_STATUS.SUCCESS);
    }
  }

  @XmlRootElement(name = "error")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class ErrorResponse extends Response implements DTOInterfaces.MESSAGE {
    private String message;

    public ErrorResponse() {
      super(COMMAND_SECTION.RESPONSE_STATUS.ERROR);
    }

    public ErrorResponse(String message) {
      this();
      this.message = message;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof ErrorResponse that)) return false;
      if (!super.equals(o)) return false;
      return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
      return Objects.hash(super.hashCode(), message);
    }

    @Override
    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  }
}
