package javachat.client.model.DTO.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import javachat.client.model.DTO.DTOInterfaces;
import lombok.EqualsAndHashCode;

import java.util.Objects;


public enum LoginSection {
  ;

  public static class Command extends CommandSection.Command implements DTOInterfaces.NAME, DTOInterfaces.PASSWORD {
    private String name;
    private String password;

    public Command() {
      super(CommandSection.LOGIN.getType());
    }

    @JsonCreator
    public Command(@JsonProperty("name") String name, @JsonProperty("password") String password) {
      this();
      this.name = name;
      this.password = password;
    }

    @Override
    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Override
    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Command command)) return false;
      if (!super.equals(o)) return false;
      return Objects.equals(name, command.name) && Objects.equals(password, command.password);
    }

    @Override
    public int hashCode() {
      return Objects.hash(super.hashCode(), name, password);
    }
  }

  @JsonSubTypes({
          @JsonSubTypes.Type(value = SuccessResponse.class, name = "success"),
          @JsonSubTypes.Type(value = ErrorResponse.class, name = "error")
  })
  public static class Response extends CommandSection.BaseResponse {
    public Response() {
      super(CommandSection.RESPONSE_STATUS.UNKNOWN);
    }

    public Response(CommandSection.RESPONSE_STATUS status) {
      super(status);
    }
  }

  @EqualsAndHashCode(callSuper = true)
  public static class SuccessResponse extends Response {
    public SuccessResponse() {
      super(CommandSection.RESPONSE_STATUS.SUCCESS);
    }
  }


  public static class ErrorResponse extends Response implements DTOInterfaces.MESSAGE {
    private String message;

    public ErrorResponse() {
      super(CommandSection.RESPONSE_STATUS.ERROR);
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
      return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
      return Objects.hash(message);
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
