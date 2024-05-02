package javachat.client.model.DTO.commands;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javachat.client.model.DTO.DTOInterfaces;

import java.util.Objects;

public enum CommandSection implements DTOInterfaces.TYPE {
  UNKNOWN("unknown"),
  MESSAGE("message"),
  LOGOUT("logout"),
  LOGIN("login"),
  LIST("list"),
  ;

  CommandSection(String name) {
    this.type = name;
  }

  private final String type;

  public String getType() {
    return this.type;
  }

  public enum RESPONSE_STATUS {
    UNKNOWN("unknown"),
    SUCCESS("success"),
    ERROR("error"),
    ;

    private final String status;

    RESPONSE_STATUS(String status) {
      this.status = status;
    }

    public String getStatus() {
      return status;
    }
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "name")
  @JsonSubTypes({
          @JsonSubTypes.Type(name = "message", value = MessageSection.Command.class),
          @JsonSubTypes.Type(name = "login", value = LoginSection.Command.class),
          @JsonSubTypes.Type(name = "logout", value = LogoutSection.Command.class),
          @JsonSubTypes.Type(name = "list", value = ListSection.Command.class)
  })
  @JacksonXmlRootElement(localName = "event")
  public static class Command implements DTOInterfaces.NAME_ATTRIBUTE {
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private String nameAttribute;

    public Command() {
    }

    public Command(String name) {
      this.nameAttribute = name;
    }

    public void setNameAttribute(String name) {
      this.nameAttribute = name;
    }

    public String toString() {
      return "RequestDTO.Commands.Command(name=" + this.getNameattribute() + ")";
    }

    public String getNameattribute() {
      return this.nameAttribute;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Command that)) return false;
      return Objects.equals(nameAttribute, that.nameAttribute);
    }

    @Override
    public int hashCode() {
      return Objects.hash(nameAttribute);
    }
  }

  public static class BaseResponse implements DTOInterfaces.STATUS {
    private RESPONSE_STATUS status;

    public BaseResponse(RESPONSE_STATUS status) {
      this.status = status;
    }

    @Override
    public RESPONSE_STATUS getStatus() {
      return status;
    }

    public void setStatus(RESPONSE_STATUS status) {
      this.status = status;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof BaseResponse that)) return false;
      return status == that.status;
    }

    @Override
    public int hashCode() {
      return Objects.hash(status);
    }
  }
}