package javachat.client.model.DTO.commands;

import javachat.client.model.DTO.DTOInterfaces;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.*;
import java.util.Objects;


public enum LoginSection {
  ;

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlRootElement(name = "command")
  public static class Command extends COMMAND_SECTION.Command implements DTOInterfaces.NAME, DTOInterfaces.PASSWORD {
    @XmlElement(name = "name", required = true)
    private String name;
    @XmlElement(name = "password", required = true)
    private String password;

    public Command() {
      super(COMMAND_SECTION.LOGIN.getType());
    }

    public Command(String name, String password) {
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

  @EqualsAndHashCode(callSuper = true)
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlRootElement(name = "success")
  public static class SuccessResponse extends Response {
    public SuccessResponse() {
      super(COMMAND_SECTION.RESPONSE_STATUS.SUCCESS);
    }
  }


  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlRootElement(name = "error")
  public static class ErrorResponse extends Response implements DTOInterfaces.MESSAGE {
    private String message;

    public ErrorResponse(String message) {
      this();
      this.message = message;
    }

    public ErrorResponse() {
      super(COMMAND_SECTION.RESPONSE_STATUS.ERROR);
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
