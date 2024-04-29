package javachat.client.model.DTO.commands;

import javachat.client.model.DTO.DTOInterfaces;
import javachat.client.model.DTO.RequestDTO;
import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public enum ListSection {
  ;

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "listCommand")
  @XmlRootElement(name = "command")
  public static class Command extends COMMAND_SECTION.Command {
    public Command() {
      super(COMMAND_SECTION.LIST.getType());
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
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class SuccessResponse extends Response implements DTOInterfaces.USERS {
    @XmlElementWrapper(name = "users", required = true)
    @XmlElement(name = "user")
    private List<RequestDTO.User> users;

    public SuccessResponse(List<RequestDTO.User> users) {
      super(COMMAND_SECTION.RESPONSE_STATUS.SUCCESS);
      this.users = users;
    }

    public SuccessResponse() {
      super(COMMAND_SECTION.RESPONSE_STATUS.SUCCESS);
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

  @XmlRootElement(name = "error")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class ErrorResponse extends Response implements DTOInterfaces.MESSAGE {
    @XmlElement(name = "message", required = true)
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

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  }
}

