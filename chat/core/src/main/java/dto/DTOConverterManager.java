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
import org.w3c.dom.Node;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class DTOConverterManager implements AbstractDTOConverter, AbstractXMLDTOConverterManager {
  private final Map<RequestDTO.DTO_SECTION, RequestDTO.AbstractDTOConverter> converters;
  private final DocumentBuilder builder;

  public DTOConverterManager(Properties properties) {
    this.converters = new HashMap<>(properties.size());
    try {
      this.converters.put(RequestDTO.DTO_SECTION.MESSAGE, new MessageDTO.MessageDTOConverter());
      this.converters.put(RequestDTO.DTO_SECTION.LOGOUT, new LogoutDTO.LogoutDTOConverter());
      this.converters.put(RequestDTO.DTO_SECTION.LOGIN, new LoginDTO.LoginDTOConverter());
      this.converters.put(RequestDTO.DTO_SECTION.LIST, new ListDTO.ListDTOConverter());

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      builder = Objects.requireNonNull(factory.newDocumentBuilder());
    } catch (JAXBException | IllegalArgumentException |
             NullPointerException | ParserConfigurationException e) {
      throw new UnableToCreateXMLContextManager(e);
    }
  }

  @Override
  public String serialize(RequestDTO dto) throws UnableToSerialize {
    var converter = converters.get(dto.getCommandType());
    if (converter == null) {
      throw new UnsupportedDTOType(dto.getDTOType().toString());
    }
    return converter.serialize(dto);
  }

  @Override
  public RequestDTO deserialize(Node root) throws UnableToDeserialize {
    try {
      RequestDTO.AbstractDTOConverter converter = Objects.requireNonNull(converters.get(getDTOType(root)));
      return converter.deserialize(root);
    } catch (IllegalArgumentException | NullPointerException e) {
      throw new UnsupportedDTOType(e.getMessage());
    }
  }

  @Override
  public Node getXMLTree(byte[] data) throws UnableToDeserialize {
    return getXMLTree(builder, data);
  }

  @Override
  public RequestDTO.AbstractDTOConverter getConverter(RequestDTO.DTO_SECTION section) {
    return converters.get(section);
  }
}
