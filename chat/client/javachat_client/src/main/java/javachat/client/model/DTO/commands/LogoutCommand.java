package javachat.client.model.DTO.commands;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;

public class LogoutCommand {
  @Data
  @EqualsAndHashCode(callSuper = true)
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Logout extends COMMAND_SECTION.Command {
    public Logout() {
      super(COMMAND_SECTION.LOGOUT.getType());
    }
  }

  @XmlSeeAlso({LogoutSuccessResponse.class, LogoutErrorResponse.class})
  public static class LogoutResponse extends COMMAND_SECTION.Response {
  }

  @EqualsAndHashCode(callSuper = true)
  public static class LogoutSuccessResponse extends COMMAND_SECTION.SuccessResponse {
  }

  @EqualsAndHashCode(callSuper = true)
  public static class LogoutErrorResponse extends COMMAND_SECTION.ErrorResponse {
    public LogoutErrorResponse(String message) {
      super(message);
    }
  }
}
