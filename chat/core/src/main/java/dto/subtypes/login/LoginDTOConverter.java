package dto.subtypes.login;

import dto.BaseDTOConverter;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

public  class LoginDTOConverter extends BaseDTOConverter {
  public LoginDTOConverter() throws JAXBException {
    super(JAXBContext.newInstance(LoginCommand.class, LoginEvent.class, LoginError.class, LoginSuccess.class));
  }
}
