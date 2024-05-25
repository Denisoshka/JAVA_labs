package dto;

import dto.exceptions.UnableToCreateXMLContextManager;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.exceptions.UnsupportedDTOType;
import dto.interfaces.DTOConverter;
import dto.interfaces.DTOConverterManagerInterface;
import dto.interfaces.DTOInterfaces;
import dto.subtypes.*;
import jakarta.xml.bind.JAXBException;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.util.Objects.requireNonNull;

public class DTOConverterManager implements DTOConverter, DTOConverterManagerInterface {
  private final Map<RequestDTO.DTO_SECTION, DTOConverter> converters;
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

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      builder = requireNonNull(factory.newDocumentBuilder());
    } catch (JAXBException | IllegalArgumentException |
             NullPointerException | ParserConfigurationException e) {
      throw new UnableToCreateXMLContextManager(e);
    }
  }

  @Override
  public String serialize(DTOInterfaces.REQUEST_DTO dto) throws UnableToSerialize {
    var converter = converters.get(dto.getDTOSection());
    if (converter == null) {
      throw new UnsupportedDTOType(dto.getDTOType().toString());
    }
    return converter.serialize(dto);
  }

  @Override
  public DTOInterfaces.REQUEST_DTO deserialize(Document root) throws UnableToDeserialize {
    try {
      RequestDTO.DTO_TYPE type = DTOConverterManagerInterface.getDTOType(root);
      RequestDTO.DTO_SECTION section = null;
      if (type == RequestDTO.DTO_TYPE.EVENT) {
        section = requireNonNull(DTOConverterManagerInterface.getDTOEvent(root)).geDTOSection();
      } else if (type == RequestDTO.DTO_TYPE.COMMAND) {
        section = requireNonNull(DTOConverterManagerInterface.getDTOCommand(root)).geDTOSection();
      }
      return converters.get(section).deserialize(root);
    } catch (IllegalArgumentException e) {
      throw new UnableToDeserialize(e);
    } catch (NullPointerException e) {
      throw new UnableToDeserialize(STR."unsupported DTO type: \{DTOConverterManagerInterface.getSTRDTOType(root)}");
    }
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
}
