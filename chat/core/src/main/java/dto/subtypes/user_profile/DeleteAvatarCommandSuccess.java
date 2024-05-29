package dto.subtypes.user_profile;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "success")
public class DeleteAvatarCommandSuccess implements DTOInterfaces.SUCCESS_RESPONSE_DTO {
  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return RequestDTO.DTO_SECTION.USERPROFILE;
  }
}
