package dto.subtypes.user_profile;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

import static dto.RequestDTO.COMMAND_TYPE.UPDATEAVATAR;

@XmlRootElement(name = "command")
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateAvatarCommand implements DTOInterfaces.COMMAND_DTO, DTOInterfaces.CONTENT, DTOInterfaces.MIME_TYPE, DTOInterfaces.SIZE {
  @XmlAttribute(name = "name")
  private final static String nameAttribute = UPDATEAVATAR.getName();
  private String mimeType;
  private byte[] content;
  private long size;

  public UpdateAvatarCommand() {
  }

  public UpdateAvatarCommand(String mimeType, long size, byte[] content) {
    this.mimeType = mimeType;
    this.size = size;
    this.content = content;
  }

  @Override
  public String getNameAttribute() {
    return nameAttribute;
  }

  @Override
  public RequestDTO.COMMAND_TYPE getCommandType() {
    return UPDATEAVATAR;
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return UPDATEAVATAR.geDTOSection();
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
  public long getSize() {
    return size;
  }
}

