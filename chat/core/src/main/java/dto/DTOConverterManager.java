package dto;

import dto.exceptions.UnableToCreateXMLContextManager;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.exceptions.UnsupportedDTOType;
import dto.interfaces.DTOConverter;
import dto.interfaces.DTOConverterManagerInterface;
import dto.subtypes.*;
import org.w3c.dom.Document;

import jakarta.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.util.Objects.requireNonNull;

public class DTOConverterManager implements DTOConverter, DTOConverterManagerInterface {
  private final Map<RequestDTO.DTO_SECTION, DTOConverter> converters;
  private final Map<RequestDTO.EVENT_TYPE, RequestDTO.DTO_SECTION> sectionEventDisplay = new HashMap<>();
  private final Map<RequestDTO.COMMAND_TYPE, RequestDTO.DTO_SECTION> sectionCommandDisplay = new HashMap<>();
  private final DocumentBuilder builder;

  public DTOConverterManager(Properties properties) {
    /*todo make properties usage*/
    this.converters = new HashMap<>();
    try {
      converters.put(RequestDTO.DTO_SECTION.LIST, new ListDTO.ListDTOConverter());
      converters.put(RequestDTO.DTO_SECTION.LOGIN, new LoginDTO.LoginDTOConverter());
      converters.put(RequestDTO.DTO_SECTION.LOGOUT, new LogoutDTO.LogoutDTOConverter());
      converters.put(RequestDTO.DTO_SECTION.MESSAGE, new MessageDTO.MessageDTOConverter());
      converters.put(RequestDTO.DTO_SECTION.FILE, new FileDTO.FileDTOConverter());
//      todo put heer FILE section
      fillEventDisplay();
      fillCommandDisplay();

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
      RequestDTO.DTO_TYPE type = DTOConverterManagerInterface.getDTOType(root);
      RequestDTO.DTO_SECTION section;
      if (type == RequestDTO.DTO_TYPE.EVENT) {
        section = sectionEventDisplay.get(DTOConverterManagerInterface.getDTOEvent(root));
      } else if (type == RequestDTO.DTO_TYPE.COMMAND) {
        section = sectionCommandDisplay.get(DTOConverterManagerInterface.getDTOCommand(root));
      } else throw new UnableToDeserialize(STR."unsupported DTO type: \{type}");
      return converters.get(section).deserialize(root);
    } catch (IllegalArgumentException | NullPointerException e) {
      throw new UnableToDeserialize(e);
    }
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSectionByEventType(RequestDTO.BaseEvent.EVENT_TYPE eventType) {
    return sectionEventDisplay.get(eventType);
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSectionByCommandType(RequestDTO.COMMAND_TYPE commandType) {
    return sectionCommandDisplay.get(commandType);
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
  public DTOConverter getConverterBySection(RequestDTO.DTO_SECTION section) {
    return converters.get(section);
  }

  @Override
  public DTOConverter getConverterByCommand(RequestDTO.COMMAND_TYPE commandType) {
    return converters.get(sectionCommandDisplay.get(commandType));
  }

  private void fillEventDisplay() {
    sectionEventDisplay.put(RequestDTO.EVENT_TYPE.MESSAGE, RequestDTO.DTO_SECTION.MESSAGE);
    sectionEventDisplay.put(RequestDTO.EVENT_TYPE.USERLOGIN, RequestDTO.DTO_SECTION.LOGIN);
    sectionEventDisplay.put(RequestDTO.EVENT_TYPE.USERLOGOUT, RequestDTO.DTO_SECTION.LOGOUT);
    sectionEventDisplay.put(RequestDTO.EVENT_TYPE.FILE, RequestDTO.DTO_SECTION.FILE);
  }

  private void fillCommandDisplay() {
    sectionCommandDisplay.put(RequestDTO.COMMAND_TYPE.MESSAGE, RequestDTO.DTO_SECTION.MESSAGE);
    sectionCommandDisplay.put(RequestDTO.COMMAND_TYPE.LOGOUT, RequestDTO.DTO_SECTION.LOGOUT);
    sectionCommandDisplay.put(RequestDTO.COMMAND_TYPE.LOGIN, RequestDTO.DTO_SECTION.LOGIN);
    sectionCommandDisplay.put(RequestDTO.COMMAND_TYPE.LIST, RequestDTO.DTO_SECTION.LIST);
    sectionCommandDisplay.put(RequestDTO.COMMAND_TYPE.DOWNLOAD, RequestDTO.DTO_SECTION.FILE);
    sectionCommandDisplay.put(RequestDTO.COMMAND_TYPE.UPLOAD, RequestDTO.DTO_SECTION.FILE);
  }
}
