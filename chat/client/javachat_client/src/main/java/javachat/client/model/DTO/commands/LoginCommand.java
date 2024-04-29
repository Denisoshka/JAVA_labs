package javachat.client.model.DTO.commands;

import javachat.client.model.DTO.DTOInterfaces;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginCommand extends COMMAND_SECTION.Command implements DTOInterfaces.NAME, DTOInterfaces.PASSWORD {
  private String name;
  private String password;

  public LoginCommand(String name, String password) {
    super(COMMAND_SECTION.LOGIN.getType());
    this.name = name;
    this.password = password;
  }

  @XmlSeeAlso({LoginSuccessResponse.class, LoginErrorResponse.class})
  public static class LoginResponse extends COMMAND_SECTION.Response {
  }

  @EqualsAndHashCode(callSuper = true)
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class LoginSuccessResponse extends COMMAND_SECTION.SuccessResponse {
  }

  @EqualsAndHashCode(callSuper = true)
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class LoginErrorResponse extends COMMAND_SECTION.ErrorResponse {
    public LoginErrorResponse(String message) {
      super(message);
    }
  }
}
