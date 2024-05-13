package dto.interfaces;

import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface AbstractDTOConverter {
  default Node getXMLTree(DocumentBuilder builder, byte[] data) throws UnableToDeserialize {
    try {
      return builder.parse(new InputSource(new ByteArrayInputStream(data)));
    } catch (SAXException | IOException e) {
      throw new UnableToDeserialize(e);
    }
  }

  default Node getXMLTree(DocumentBuilder builder, String xml) throws UnableToDeserialize {
    return getXMLTree(builder, xml.getBytes());
  }

  String serialize(RequestDTO dto) throws UnableToSerialize;

  RequestDTO deserialize(Node root) throws UnableToDeserialize;

  Node getXMLTree(byte[] data) throws UnableToDeserialize;
}

