package dto.subtypes.list;

import dto.BaseDTOConverter;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

public  class ListDTOConverter extends BaseDTOConverter {
  public ListDTOConverter() throws JAXBException {
    super(JAXBContext.newInstance(ListCommand.class, ListError.class, ListSuccess.class));
  }
}
