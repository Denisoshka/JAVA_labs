package dto.interfaces;

import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;

public interface DTOConverter {
  default Document getXMLTree(DocumentBuilder builder, byte[] data) throws UnableToDeserialize {
    try {
      return builder.parse(new InputSource(new ByteArrayInputStream(data)));
    } catch (SAXException | IOException e) {
      throw new UnableToDeserialize(e);
    }
  }

  default Document getXMLTree(DocumentBuilder builder, String xml) throws UnableToDeserialize {
    try {
      InputSource is = new InputSource();
      is.setCharacterStream(new StringReader(xml));
      return builder.parse(is);
    } catch (IOException | SAXException e) {
      throw new UnableToDeserialize(e);
    }
  }

  String serialize(RequestDTO dto) throws UnableToSerialize;

  RequestDTO deserialize(Document root) throws UnableToDeserialize;
}

