package javachat.client.model.DTO.commands;

import javachat.client.model.DTO.DTOInterfaces;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.Objects;

public enum COMMAND_SECTION implements DTOInterfaces.TYPE {
  UNKNOWN("unknown"),
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

  @Getter
  @Setter
  @Data
  @XmlAccessorType(XmlAccessType.FIELD)
//  @XmlRootElement(name = "command")
  @XmlType(name = "baseCommand")
  @XmlSeeAlso({MessageSection.class, ListSection.class, LoginSection.class, LoginSection.class})
  public static class Command implements DTOInterfaces.NAME_ATTRIBUTE {
    @XmlAttribute(name = "name")
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
      return "RequestDTO.Commands.Command(name=" + this.getNameAttribute() + ")";
    }

    public String getNameAttribute() {
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

  @EqualsAndHashCode
  public static class BaseResponse implements DTOInterfaces.STATUS {

    private RESPONSE_STATUS status;

    public BaseResponse() {
    }

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

  }


/*  @Getter
  @Setter
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class SuccessResponse extends Response {
    public SuccessResponse() {
      super(RESPONSE_STATUS.SUCCESS);
    }
  }

  @Getter
  @Setter
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class ErrorResponse extends Response implements DTOInterfaces.MESSAGE {
    @XmlElement(name = "message", required = true)
    private String message;

    public ErrorResponse() {
      super(RESPONSE_STATUS.ERROR);
    }

    public ErrorResponse(String message) {
      this();
      this.message = message;
    }
  }*/
}