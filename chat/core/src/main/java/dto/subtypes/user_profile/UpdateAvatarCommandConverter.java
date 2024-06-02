package dto.subtypes.user_profile;

import dto.BaseDTOConverter;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

public class UpdateAvatarCommandConverter extends BaseDTOConverter {
  public UpdateAvatarCommandConverter() throws JAXBException {
    super(JAXBContext.newInstance(UpdateAvatarCommand.class, UpdateAvatarEvent.class, UpdateAvatarCommandSuccess.class, UserProfileCommandError.class));
  }
}
