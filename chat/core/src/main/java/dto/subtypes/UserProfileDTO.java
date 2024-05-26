package dto.subtypes;

import dto.BaseDTOConverter;
import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.interfaces.DTOConverter;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.w3c.dom.Document;

import static dto.RequestDTO.COMMAND_TYPE.DELETEAVATAR;
import static dto.RequestDTO.COMMAND_TYPE.UPDATEAVATAR;

public class UserProfileDTO {
  public static class UpdateAvatarCommandConverter extends BaseDTOConverter {
    public UpdateAvatarCommandConverter() throws JAXBException {
      super(JAXBContext.newInstance(UpdateAvatarCommand.class, UpdateAvatarEvent.class, UpdateAvatarCommandSuccess.class, UserProfileCommandError.class));
    }
  }

  public static class DeleteAvatarCommandConverter extends BaseDTOConverter {
    public DeleteAvatarCommandConverter() throws JAXBException {
      super(JAXBContext.newInstance(DeleteAvatarCommand.class, DeleteAvatarEvent.class, DeleteAvatarCommandSuccess.class, UserProfileCommandError.class));
    }
  }

  public static class UserProfileDTOConverter implements DTOConverter {
    private final UpdateAvatarCommandConverter updateAvatarCommandConverter;
    private final DeleteAvatarCommandConverter deleteAvatarCommandConverter;

    public UserProfileDTOConverter() throws JAXBException {
      super();
      this.updateAvatarCommandConverter = new UpdateAvatarCommandConverter();
      this.deleteAvatarCommandConverter = new DeleteAvatarCommandConverter();
    }

    @Override
    public String serialize(DTOInterfaces.REQUEST_DTO dto) throws UnableToSerialize {
      return null;
    }

    @Override
    public DTOInterfaces.REQUEST_DTO deserialize(Document root) throws UnableToDeserialize {
      return null;
    }

    public DeleteAvatarCommandConverter getDeleteAvatarCommandConverter() {
      return deleteAvatarCommandConverter;
    }

    public UpdateAvatarCommandConverter getUpdateAvatarCommandConverter() {
      return updateAvatarCommandConverter;
    }
  }


  @XmlRootElement(name = "command")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class UpdateAvatarCommand implements DTOInterfaces.COMMAND_DTO, DTOInterfaces.CONTENT, DTOInterfaces.MIME_TYPE, DTOInterfaces.SIZE {
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

  @XmlRootElement(name = "success")
  public static class UpdateAvatarCommandSuccess implements DTOInterfaces.SUCCESS_RESPONSE_DTO {
    @Override
    public RequestDTO.DTO_SECTION getDTOSection() {
      return RequestDTO.DTO_SECTION.USERPROFILE;
    }
  }

  @XmlRootElement(name = "event")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class UpdateAvatarEvent implements DTOInterfaces.EVENT_DTO, DTOInterfaces.CONTENT, DTOInterfaces.MIME_TYPE {
    @XmlAttribute(name = "name")
    private final String nameAttribute = RequestDTO.EVENT_TYPE.UPDATEAVATAR.getName();
    private byte[] content;
    private String mimeType;

    public UpdateAvatarEvent() {
    }

    public UpdateAvatarEvent(byte[] content, String mimeType) {
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
  }


  @XmlRootElement(name = "command")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class DeleteAvatarCommand implements DTOInterfaces.COMMAND_DTO {
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


  public static class DeleteAvatarEvent implements DTOInterfaces.EVENT_DTO {
    @XmlAttribute(name = "name")
    private final static String nameAttribute = RequestDTO.EVENT_TYPE.DELETEAVATAR.getName();

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
  }

  @XmlRootElement(name = "success")
  public static class DeleteAvatarCommandSuccess implements DTOInterfaces.SUCCESS_RESPONSE_DTO {
    @Override
    public RequestDTO.DTO_SECTION getDTOSection() {
      return RequestDTO.DTO_SECTION.USERPROFILE;
    }
  }

  @XmlRootElement(name = "error")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class UserProfileCommandError implements DTOInterfaces.ERROR_RESPONSE_DTO {
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
}
