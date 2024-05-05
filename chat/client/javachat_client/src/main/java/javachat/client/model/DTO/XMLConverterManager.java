package javachat.client.model.DTO;

import javachat.client.model.DTO.exceptions.UnableToCreateXMLContextManager;
import javachat.client.model.DTO.exceptions.UnableToDeserialize;
import javachat.client.model.DTO.exceptions.UnableToSerialize;
import javachat.client.model.DTO.exceptions.UnsupportedDTOType;
import javachat.client.model.DTO.interfaces.DTOConverterInterface;
import javachat.client.model.DTO.interfaces.XMLConverterManageInterface;
import org.w3c.dom.Node;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class XMLConverterManager implements DTOConverterInterface, XMLConverterManageInterface {
  private final Map<RequestDTO.DTO_TYPE, RequestDTO.DTOConverter> converters;

  public XMLConverterManager(Properties properties) {
    this.converters = new HashMap<>(properties.size());
    try {
      for (String property : properties.stringPropertyNames()) {
        converters.put(
                RequestDTO.DTO_TYPE.valueOf(property),
                (RequestDTO.DTOConverter) Class.forName(property).getDeclaredConstructor().newInstance()
        );
      }
    } catch (
            IllegalArgumentException | ClassNotFoundException | NoSuchMethodException |
            IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw new UnableToCreateXMLContextManager(e);
    }
  }

  @Override
  public String serialize(RequestDTO dto) throws UnableToSerialize {
    var converter = converters.get(dto.getDTOType());
    if (converter == null) {
      throw new UnsupportedDTOType(dto.getDTOType().toString());
    }
    return converters.get(dto.getDTOType()).serialize(dto);
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
}
