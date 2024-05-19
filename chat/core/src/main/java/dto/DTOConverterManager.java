package dto;

import dto.exceptions.UnableToCreateXMLContextManager;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.exceptions.UnsupportedDTOType;
import dto.interfaces.DTOConverter;
import dto.interfaces.XMLDTOConverterManager;
import dto.subtypes.*;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.util.Objects.requireNonNull;

public class DTOConverterManager implements DTOConverter, XMLDTOConverterManager {
  private final Map<RequestDTO.DTO_SECTION, DTOConverter> converters;
  private final Map<RequestDTO.BaseEvent.EVENT_TYPE, RequestDTO.DTO_SECTION> sectionEventDisplay;
  private final DocumentBuilder builder;

  public DTOConverterManager(Properties properties) {
    /*todo make properties usage*/
    this.converters = new HashMap<>();
    this.sectionEventDisplay = new HashMap<>();
    try {
      converters.put(RequestDTO.DTO_SECTION.LIST, new ListDTO.ListDTOConverter());
      converters.put(RequestDTO.DTO_SECTION.BASE, new RequestDTO.BaseDTOConverter());
      converters.put(RequestDTO.DTO_SECTION.LOGIN, new LoginDTO.LoginDTOConverter());
      converters.put(RequestDTO.DTO_SECTION.LOGOUT, new LogoutDTO.LogoutDTOConverter());
      converters.put(RequestDTO.DTO_SECTION.MESSAGE, new MessageDTO.MessageDTOConverter());
      converters.put(RequestDTO.DTO_SECTION.FILE, new FileDTO.FileDTOConverter());
//      todo put heer FILE section
      sectionEventDisplay.put(RequestDTO.EVENT_TYPE.BASE, RequestDTO.DTO_SECTION.BASE);
      sectionEventDisplay.put(RequestDTO.EVENT_TYPE.MESSAGE, RequestDTO.DTO_SECTION.MESSAGE);
      sectionEventDisplay.put(RequestDTO.EVENT_TYPE.USERLOGIN, RequestDTO.DTO_SECTION.LOGIN);
      sectionEventDisplay.put(RequestDTO.EVENT_TYPE.USERLOGOUT, RequestDTO.DTO_SECTION.LOGOUT);
      sectionEventDisplay.put(RequestDTO.EVENT_TYPE.FILE, RequestDTO.DTO_SECTION.FILE);
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
      RequestDTO.DTO_SECTION type = XMLDTOConverterManager.getDTOType(root) == RequestDTO.DTO_TYPE.EVENT ?
              sectionEventDisplay.get(XMLDTOConverterManager.getDTOEvent(root))
              : XMLDTOConverterManager.getDTOSection(root);
      return requireNonNull(converters.get(type).deserialize(root));
    } catch (IllegalArgumentException | NullPointerException e) {
      throw new UnsupportedDTOType(e);
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
  public DTOConverter getConverter(RequestDTO.DTO_SECTION section) {
    return converters.get(section);
  }
}
