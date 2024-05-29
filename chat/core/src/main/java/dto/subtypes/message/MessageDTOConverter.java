package dto.subtypes.message;

import dto.BaseDTOConverter;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

public  class MessageDTOConverter extends BaseDTOConverter {
  public MessageDTOConverter() throws JAXBException {
    super(JAXBContext.newInstance(MessageDTO.Command.class, MessageDTO.Event.class, MessageDTO.Error.class, MessageDTO.Success.class));
  }
}