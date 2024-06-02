package dto.subtypes.user_profile;

import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.interfaces.DTOConverter;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.JAXBException;
import org.w3c.dom.Document;

public class UserProfileDTOConverter implements DTOConverter {
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

