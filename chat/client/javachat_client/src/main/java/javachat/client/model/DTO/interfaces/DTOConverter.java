package javachat.client.model.DTO.interfaces;

import javachat.client.model.DTO.RequestDTO;
import javachat.client.model.DTO.exceptions.UnableToDeserialize;
import javachat.client.model.DTO.exceptions.UnableToSerialize;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface DTOConverter {
    default Node getXMLTree(DocumentBuilder builder, byte[] xml) throws IOException, SAXException {
    return builder.parse(new InputSource(new ByteArrayInputStream(xml)));
  }

  default Node getXMLTree(DocumentBuilder builder, String xml) throws IOException, SAXException {
    return getXMLTree(builder, xml.getBytes());
  }

  String serialize(RequestDTO dto) throws UnableToSerialize;

  RequestDTO deserialize(Node root) throws UnableToDeserialize;
}

