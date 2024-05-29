package dto.subtypes.user_profile;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "event")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteAvatarEvent implements DTOInterfaces.EVENT_DTO, DTOInterfaces.NAME {
  @XmlAttribute(name = "name")
  private final static String nameAttribute = RequestDTO.EVENT_TYPE.DELETEAVATAR.getName();
  private String name;

  public DeleteAvatarEvent() {
  }

  public DeleteAvatarEvent(String name) {
    this.name = name;
  }

  @Override
  public RequestDTO.EVENT_TYPE getEventType() {
    return RequestDTO.EVENT_TYPE.DELETEAVATAR;
  }

  @Override
  public String getNameAttribute() {
    return nameAttribute;
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return RequestDTO.DTO_SECTION.USERPROFILE;
  }

  @Override
  public String getName() {
    return name;
  }
}

