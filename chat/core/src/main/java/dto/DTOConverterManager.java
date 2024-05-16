package dto;

import dto.exceptions.UnableToCreateXMLContextManager;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.exceptions.UnsupportedDTOType;
import dto.interfaces.AbstractDTOConverter;
import dto.interfaces.AbstractXMLDTOConverterManager;
import dto.subtypes.ListDTO;
import dto.subtypes.LoginDTO;
import dto.subtypes.LogoutDTO;
import dto.subtypes.MessageDTO;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.util.Objects.requireNonNull;

public class DTOConverterManager implements AbstractDTOConverter, AbstractXMLDTOConverterManager {
  private final Map<RequestDTO.DTO_SECTION, RequestDTO.AbstractDTOConverter> converters;
  private final Map<RequestDTO.BaseEvent.EVENT_TYPE, RequestDTO.DTO_SECTION> sectionEventDisplay;
  private final DocumentBuilder builder;

  public DTOConverterManager(Properties properties) {
    /*todo make properties usage*/
    this.converters = new HashMap<>();
    this.sectionEventDisplay = new HashMap<>();
    try {
      converters.put(RequestDTO.DTO_SECTION.MESSAGE, new MessageDTO.MessageDTOConverter());
      converters.put(RequestDTO.DTO_SECTION.LOGOUT, new LogoutDTO.LogoutDTOConverter());
      converters.put(RequestDTO.DTO_SECTION.LOGIN, new LoginDTO.LoginDTOConverter());
      converters.put(RequestDTO.DTO_SECTION.LIST, new ListDTO.ListDTOConverter());
      sectionEventDisplay.put(RequestDTO.BaseEvent.EVENT_TYPE.MESSAGE, RequestDTO.DTO_SECTION.MESSAGE);
      sectionEventDisplay.put(RequestDTO.BaseEvent.EVENT_TYPE.USERLOGOUT, RequestDTO.DTO_SECTION.LOGOUT);
      sectionEventDisplay.put(RequestDTO.BaseEvent.EVENT_TYPE.USERLOGIN, RequestDTO.DTO_SECTION.LOGIN);


      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      builder = requireNonNull(factory.newDocumentBuilder());
    } catch (JAXBException | IllegalArgumentException |
             NullPointerException | ParserConfigurationException e) {
      throw new UnableToCreateXMLContextManager(e);
    }
  }

  @Override
  public String serialize(RequestDTO dto) throws UnableToSerialize {
    var converter = converters.get(dto.geDTOSection());
    if (converter == null) {
      throw new UnsupportedDTOType(dto.getDTOType().toString());
    }
    return converter.serialize(dto);
  }

  @Override
  public RequestDTO deserialize(Document root) throws UnableToDeserialize {
    try {
      RequestDTO.DTO_SECTION type = getDTOType(root) == RequestDTO.DTO_TYPE.EVENT ? sectionEventDisplay.get(getDTOEvent(root)) : getDTOSection(root);
      return requireNonNull(converters.get(type).deserialize(root));
    } catch (IllegalArgumentException | NullPointerException e) {
      throw new UnsupportedDTOType(e.getMessage());
    }
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSectionByEventType(RequestDTO.BaseEvent.EVENT_TYPE eventType) {
    return sectionEventDisplay.get(eventType);
  }

  @Override
  public Document getXMLTree(byte[] data) throws UnableToDeserialize {
    return getXMLTree(builder, data);
  }

  @Override
  public Document getXMLTree(String data) throws UnableToDeserialize {
    return getXMLTree(builder, data);
  }

  @Override
  public RequestDTO.AbstractDTOConverter getConverter(RequestDTO.DTO_SECTION section) {
    return converters.get(section);
  }
}
