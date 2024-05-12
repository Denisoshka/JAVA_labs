package dto;

import dto.exceptions.UnableToCreateXMLContextManager;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.exceptions.UnsupportedDTOType;
import dto.interfaces.DTOConverter;
import dto.interfaces.XMLDTOConverterManager;
import dto.subtypes.ListDTO;
import dto.subtypes.LoginDTO;
import dto.subtypes.LogoutDTO;
import dto.subtypes.MessageDTO;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class DTOConverterManager implements DTOConverter, XMLDTOConverterManager {
  private final Map<RequestDTO.DTO_SECTION, RequestDTO.DTOConverter> converters;

  public DTOConverterManager(Properties properties) {
    this.converters = new HashMap<>(properties.size());
    try {
      this.converters.put(RequestDTO.DTO_SECTION.MESSAGE, new MessageDTO.MessageDTOConverter());
      this.converters.put(RequestDTO.DTO_SECTION.LOGOUT, new LogoutDTO.LogoutDTOConverter());
      this.converters.put(RequestDTO.DTO_SECTION.LOGIN, new LoginDTO.LoginDTOConverter());
      this.converters.put(RequestDTO.DTO_SECTION.LIST, new ListDTO.ListDTOConverter());
      /*for (String property : properties.stringPropertyNames()) {
        converters.put(
                RequestDTO.DTO_SECTION.valueOf(property),
                (RequestDTO.DTOConverter) Class.forName(property).getDeclaredConstructor().newInstance()
        );
      }*/
    } catch (
            JAXBException | IllegalArgumentException e) {
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
      RequestDTO.DTOConverter converter = Objects.requireNonNull(converters.get(getDTOType(root)));
      return converter.deserialize(root);
    } catch (IllegalArgumentException | NullPointerException e) {
      throw new UnsupportedDTOType(e.getMessage());
    }
  }

  @Override
  public RequestDTO.DTOConverter getConverter(RequestDTO.DTO_SECTION section) {
    return converters.get(section);
  }
}
