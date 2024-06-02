package dto.subtypes.user_profile;


import dto.BaseDTOConverter;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

public class DeleteAvatarCommandConverter extends BaseDTOConverter {
  public DeleteAvatarCommandConverter() throws JAXBException {
    super(JAXBContext.newInstance(DeleteAvatarCommand.class, DeleteAvatarEvent.class, DeleteAvatarCommandSuccess.class, UserProfileCommandError.class));
  }
}

