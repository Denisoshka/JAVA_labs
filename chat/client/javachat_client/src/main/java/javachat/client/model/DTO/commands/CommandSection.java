package javachat.client.model.DTO.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javachat.client.model.DTO.DTOInterfaces;

import java.util.Objects;

public enum CommandSection {
  ;

  public enum COMMANDS {
    BASE("base"),
    MESSAGE("message"),
    LOGOUT("logout"),
    LOGIN("login"),
    LIST("list"),
    ;

    COMMANDS(String name) {
      this.type = name;
    }

    private final String type;

    public String getType() {
      return this.type;
    }

  }

  public enum RESPONSES {
    UNKNOWN("unknown"),
    SUCCESS("success"),
    ERROR("error"),
    ;

    private final String type;

    RESPONSES(String status) {
      this.type = status;
    }

    public String getType() {
      return type;
    }
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "name")
  @JsonSubTypes({
          @JsonSubTypes.Type(name = "base", value = Command.class),
          @JsonSubTypes.Type(name = "message", value = MessageSection.Command.class),
          @JsonSubTypes.Type(name = "login", value = LoginSection.Command.class),
          @JsonSubTypes.Type(name = "logout", value = LogoutSection.Command.class),
          @JsonSubTypes.Type(name = "list", value = ListSection.Command.class)
  })
  @JacksonXmlRootElement(localName = "command")
  public static class Command implements DTOInterfaces.COMMAND_TYPE {
    @JsonIgnore
    COMMANDS commandType;

    public Command(COMMANDS type) {
      this.commandType = type;
    }


    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Command that)) return false;
      return Objects.equals(commandType, that.commandType);
    }

    @Override
    public int hashCode() {
      return Objects.hash(commandType);
    }

    public COMMANDS getCommandType() {
      return commandType;
    }

    public void setCommandType(COMMANDS commandType) {
      this.commandType = commandType;
    }
  }

  public static class BaseResponse implements DTOInterfaces.RESPONSE_TYPE {
    private RESPONSES responseStatus;

    public BaseResponse(RESPONSES responseStatus) {
      this.responseStatus = responseStatus;
    }

    @Override
    public RESPONSES getResponseType() {
      return responseStatus;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof BaseResponse that)) return false;
      return responseStatus == that.responseStatus;
    }

    @Override
    public int hashCode() {
      return Objects.hash(responseStatus);
    }
  }
}