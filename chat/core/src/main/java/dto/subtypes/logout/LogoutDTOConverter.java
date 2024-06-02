package dto.subtypes.logout;

import dto.BaseDTOConverter;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

public class LogoutDTOConverter extends BaseDTOConverter {
  public LogoutDTOConverter() throws JAXBException {
    super(JAXBContext.newInstance(LogoutCommand.class, LogoutEvent.class, LogoutError.class, LogoutSuccess.class));
  }
}
