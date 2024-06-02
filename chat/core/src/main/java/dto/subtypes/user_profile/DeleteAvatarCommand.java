package dto.subtypes.user_profile;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

import static dto.RequestDTO.COMMAND_TYPE.DELETEAVATAR;

@XmlRootElement(name = "command")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteAvatarCommand implements DTOInterfaces.COMMAND_DTO {
  @XmlAttribute(name = "name")
  private final static String nameAttribute = DELETEAVATAR.getName();

  @Override
  public String getNameAttribute() {
    return nameAttribute;
  }

  @Override
  public RequestDTO.COMMAND_TYPE getCommandType() {
    return DELETEAVATAR;
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return DELETEAVATAR.geDTOSection();
  }
}

