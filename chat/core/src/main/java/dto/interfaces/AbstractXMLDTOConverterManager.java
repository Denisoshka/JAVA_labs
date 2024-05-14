package dto.interfaces;

import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface AbstractXMLDTOConverterManager extends AbstractDTOConverter {
  /**
   * return {@code RequestDTO.DTO_TYPE} which specified in root node if {@code RequestDTO.DTO_TYPE} exists, else {@code null}
   */
  default RequestDTO.DTO_TYPE getDTOType(Document root) {
    try {
      return RequestDTO.DTO_TYPE.valueOf(root.getDocumentElement().getNodeName().toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  /**
   * return {@code RequestDTO.DTO_SECTION} which specified in root node if {@code RequestDTO.DTO_SECTION} exists, else {@code null}
   */
  default RequestDTO.DTO_SECTION getDTOSection(Document root) {
    try {
      return RequestDTO.DTO_SECTION.valueOf(root.getDocumentElement().getAttribute("name").toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  default RequestDTO.BaseEvent.EVENT_TYPE getDTOEventType(Document root) {
    try {
      return RequestDTO.BaseEvent.EVENT_TYPE.valueOf(root.getDocumentElement().getAttribute("name").toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }


  Node getXMLTree(byte[] data) throws UnableToDeserialize;

  Node getXMLTree(String data) throws UnableToDeserialize;

  default String getDTOName(Node root) {
    return root.getAttributes().getNamedItem("name").getNodeValue();
  }

  RequestDTO.AbstractDTOConverter getConverter(RequestDTO.DTO_SECTION section);
}
