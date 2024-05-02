package javachat.client.model.DTO.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import javachat.client.model.DTO.DTOInterfaces;
import lombok.EqualsAndHashCode;

import java.util.Objects;

import static javachat.client.model.DTO.commands.CommandSection.COMMANDS.MESSAGE;


public enum MessageSection {
  ;

  public static final class Command extends CommandSection.Command implements DTOInterfaces.MESSAGE {
    private String message;

    public Command()  {
      super(MESSAGE);
    }

    @JsonCreator
    public Command(@JsonProperty("message") String message) {
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
  public static class Response extends CommandSection.BaseResponse {
    public Response() {
      super(CommandSection.RESPONSES.UNKNOWN);
    }

    public Response(CommandSection.RESPONSES status) {
      super(status);
    }
  }

  @EqualsAndHashCode(callSuper = true)
  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "type")
  @JsonSubTypes({
          @JsonSubTypes.Type(value = SuccessResponse.class, name = "success"),
          @JsonSubTypes.Type(value = ErrorResponse.class, name = "error")
  })
  public static class SuccessResponse extends Response {
    public SuccessResponse() {
      super(CommandSection.RESPONSES.SUCCESS);
    }
  }

  public static class ErrorResponse extends Response implements DTOInterfaces.MESSAGE {
    private String message;

    public ErrorResponse() {
      super(CommandSection.RESPONSES.ERROR);
    }

    @JsonCreator
    public ErrorResponse(@JsonProperty("message") String message) {
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
