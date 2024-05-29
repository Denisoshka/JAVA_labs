package dto.subtypes.message;

import dto.BaseDTOConverter;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

public  class MessageDTOConverter extends BaseDTOConverter {
  public MessageDTOConverter() throws JAXBException {
    super(JAXBContext.newInstance(MessageCommand.class, MessageEvent.class, MessageError.class, MessageSuccess.class));
  }
}