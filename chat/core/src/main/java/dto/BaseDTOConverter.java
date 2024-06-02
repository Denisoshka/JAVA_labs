package dto;

import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.interfaces.DTOConverter;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.w3c.dom.Document;

import java.io.StringWriter;

public class BaseDTOConverter implements DTOConverter {
  final JAXBContext context;
  final Unmarshaller unmarshaller;
  final Marshaller marshaller;

  public BaseDTOConverter(JAXBContext context) throws JAXBException {
    this.context = context;
    this.unmarshaller = context.createUnmarshaller();
    this.marshaller = context.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
  }

  @Override
  public String serialize(DTOInterfaces.REQUEST_DTO dto) throws UnableToSerialize {
    try {
      StringWriter writer = new StringWriter();
      synchronized (marshaller) {
        marshaller.marshal(dto, writer);
      }
      return writer.toString();
    } catch (JAXBException e) {
      throw new UnableToSerialize(e);
    }
  }

  @Override
  public DTOInterfaces.REQUEST_DTO deserialize(Document root) throws UnableToDeserialize {
    try {
      synchronized (unmarshaller) {
        return (DTOInterfaces.REQUEST_DTO) unmarshaller.unmarshal(root);
      }
    } catch (JAXBException e) {
      throw new UnableToDeserialize(e);
    }
  }
}