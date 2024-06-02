package dto.subtypes.user_profile;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "event")
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateAvatarEvent implements DTOInterfaces.EVENT_DTO, DTOInterfaces.NAME, DTOInterfaces.CONTENT, DTOInterfaces.MIME_TYPE {

  @XmlAttribute(name = "name")
  private final String nameAttribute = RequestDTO.EVENT_TYPE.UPDATEAVATAR.getName();
  private byte[] content;
  private String mimeType;
  private String name;

  public UpdateAvatarEvent() {
  }

  public UpdateAvatarEvent(String name, String mimeType, byte[] content) {
    this.name = name;
    this.content = content;
    this.mimeType = mimeType;
  }

  @Override
  public RequestDTO.EVENT_TYPE getEventType() {
    return RequestDTO.EVENT_TYPE.UPDATEAVATAR;
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
  public byte[] getContent() {
    return content;
  }

  @Override
  public String getMimeType() {
    return mimeType;
  }

  @Override
  public String getName() {
    return name;
  }
}



