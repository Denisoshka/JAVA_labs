package dto.interfaces;

import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface DTOConverterManagerInterface extends DTOConverter {
  /**
   * return {@code RequestDTO.DTO_TYPE} which specified in root node if {@code RequestDTO.DTO_TYPE} exists, else {@code null}
   */
  static RequestDTO.DTO_TYPE getDTOType(Document root) {
    try {
      return RequestDTO.DTO_TYPE.valueOf(root.getDocumentElement().getNodeName().toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  /**
   * return {@code RequestDTO.DTO_SECTION} which specified in root node if {@code RequestDTO.DTO_SECTION} exists, else {@code null}
   */
  static RequestDTO.DTO_SECTION getDTOSection(Document root) {
    try {
      return RequestDTO.DTO_SECTION.valueOf(root.getDocumentElement().getAttribute("name").toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  static RequestDTO.COMMAND_TYPE getDTOCommand(Document root) {
    try {
      return RequestDTO.COMMAND_TYPE.valueOf(root.getDocumentElement().getAttribute("name").toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  static RequestDTO.EVENT_TYPE getDTOEvent(Document root) {
    try {
      return RequestDTO.EVENT_TYPE.valueOf(root.getDocumentElement().getAttribute("name").toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  RequestDTO.DTO_SECTION getDTOSectionByEventType(RequestDTO.BaseEvent.EVENT_TYPE eventType);

  Node getXMLTree(byte[] data) throws UnableToDeserialize;

  Node getXMLTree(String data) throws UnableToDeserialize;

  default String getDTOName(Node root) {
    return root.getAttributes().getNamedItem("name").getNodeValue();
  }

  DTOConverter getConverter(RequestDTO.DTO_SECTION section);
}
