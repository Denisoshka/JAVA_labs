package dto.subtypes.user_profile;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserProfileCommandError implements DTOInterfaces.ERROR_RESPONSE_DTO {
  private String message;

  public UserProfileCommandError() {
  }

  public UserProfileCommandError(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return RequestDTO.DTO_SECTION.USERPROFILE;
  }
}