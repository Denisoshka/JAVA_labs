package javachat.client.model.DTO;

import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public interface DTOInterfaces {
  interface NAME_ATTRIBUTE {
    String getNameAttribute();
  }

  interface HOSTNAME {
    String getHostname();
  }

  interface PORT {
    int getPort();
  }

  interface PASSWORD {
    String getPassword();
  }

  interface MESSAGE {
    String getMessage();
  }

  interface NAME {
    String getName();
  }

  interface FROM {
    String getFrom();
  }

  interface USERS {
    List<XyiDTO.User> getUsers();
  }

  interface DTO_TYPE {
    RequestDTO.DTO_TYPE getDTOType();
  }

  interface EVENT_TYPE {
    RequestDTO.BaseEvent.EVENT_TYPE getEventType();
  }

  interface RESPONSE_TYPE {
    RequestDTO.BaseResponse.RESPONSE_TYPE getResponseType();
  }

  interface COMMAND_TYPE {
    RequestDTO.BaseCommand.COMMAND_TYPE getCommandType();
  }

  interface DTOConverterInterface {
    default RequestDTO.DTO_TYPE getDTOType(Node root) {
      return RequestDTO.DTO_TYPE.valueOf(root.getNodeName());
    }

    default String getDTOName(Node root) {
      return root.getAttributes().getNamedItem("name").getNodeValue();
    }

    default Node getXMLTree(DocumentBuilder builder, String xmlString) throws IOException, SAXException {
      return builder.parse(new InputSource(new StringReader(xmlString)));
    }

    String serialize(RequestDTO dto) throws JAXBException;

    RequestDTO deserialize(Node root) throws JAXBException;
  }
}
