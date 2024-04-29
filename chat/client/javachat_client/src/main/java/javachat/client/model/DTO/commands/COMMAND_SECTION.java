package javachat.client.model.DTO.commands;

import javachat.client.model.DTO.DTOInterfaces;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.Objects;

public enum COMMAND_SECTION {
  MESSAGE("message"),
  LOGOUT("logout"),
  LOGIN("login"),
  LIST("list"),
  ;

  COMMAND_SECTION(String name) {
    this.type = name;
  }

  private final String type;

  public String getType() {
    return this.type;
  }

  public enum RESPONSE_STATUS {
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

  @Getter
  @Setter
  @XmlSeeAlso({MessageCommand.class})
  @XmlRootElement(name = "command")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Command implements DTOInterfaces.NAME_ATTRIBUTE {
    @XmlAttribute(name = "name")
    private String nameAttribute;

    public Command(String name) {
      this.nameAttribute = name;
    }

    public void setName(String name) {
      this.nameAttribute = name;
    }

    public boolean equals(final Object o) {
      if (o == this) return true;
      if (!(o instanceof Command other)) return false;
      if (!other.canEqual(this)) return false;
      final Object this$name = this.getNameAttribute();
      final Object other$name = other.getNameAttribute();
      return Objects.equals(this$name, other$name);
    }

    protected boolean canEqual(final Object other) {
      return other instanceof Command;
    }

    public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      final Object $name = this.getNameAttribute();
      result = result * PRIME + ($name == null ? 43 : $name.hashCode());
      return result;
    }

    public String toString() {
      return "RequestDTO.Commands.Command(name=" + this.getNameAttribute() + ")";
    }

    public String getNameAttribute() {
      return this.nameAttribute;
    }
  }

  @Getter
  @Setter
  public static class Response implements DTOInterfaces.STATUS {
    RESPONSE_STATUS status;

    public Response(RESPONSE_STATUS status) {
      this.status = status;
    }

    @Override
    public RESPONSE_STATUS getStatus() {
      return status;
    }
  }

  @Getter
  @Setter
  @EqualsAndHashCode(callSuper = true)
  @XmlRootElement(name = "success")
  public static class SuccessResponse extends Response {
    public SuccessResponse() {
      super(RESPONSE_STATUS.SUCCESS);
    }
  }

  @Getter
  @Setter
  @EqualsAndHashCode(callSuper = true)
  @XmlRootElement(name = "error")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class ErrorResponse extends Response implements DTOInterfaces.MESSAGE {
    @XmlElement(name = "message", required = true)
    private String message;

    public ErrorResponse(String message) {
      super(RESPONSE_STATUS.ERROR);
      this.message = message;
    }
  }
}