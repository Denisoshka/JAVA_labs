package javachat.client.model.DTO.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import javachat.client.model.DTO.DTOInterfaces;
import javachat.client.model.DTO.RequestDTO;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public enum ListSection {
  ;

  public static class Command extends CommandSection.Command {
    public Command() {
      super(CommandSection.COMMANDS.LIST);
    }
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "type")
  @JsonSubTypes({
          @JsonSubTypes.Type(value = SuccessResponse.class, name = "success"),
          @JsonSubTypes.Type(value = ErrorResponse.class, name = "error")
  })
  @EqualsAndHashCode(callSuper = true)
  public static class Response extends CommandSection.BaseResponse {
    public Response() {
      super(CommandSection.RESPONSES.UNKNOWN);
    }

    public Response(CommandSection.RESPONSES status) {
      super(status);
    }
  }

  public static class SuccessResponse extends Response implements DTOInterfaces.USERS {
    @JacksonXmlElementWrapper(localName = "users")
    private List<RequestDTO.User> users;

    @JsonCreator
    public SuccessResponse(@JsonProperty("users") List<RequestDTO.User> users) {
      super(CommandSection.RESPONSES.SUCCESS);
      this.users = users;
    }

    @JsonCreator
    public SuccessResponse() {
      super(CommandSection.RESPONSES.SUCCESS);
      users = new ArrayList<>();
    }

    public List<RequestDTO.User> getUsers() {
      return this.users;
    }

    public void setUsers(List<RequestDTO.User> users) {
      this.users = users;
    }

    public String toString() {
      return "ListCommand.SuccessResponse(users=" + this.getUsers() + ")";
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof SuccessResponse that)) return false;
      if (!super.equals(o)) return false;
      return Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
      return Objects.hash(super.hashCode(), users);
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

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  }
}

