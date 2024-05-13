package dto.interfaces;

import dto.RequestDTO;
import org.w3c.dom.Node;

public interface AbstractXMLDTOConverterManager {
  /**
   * return {@code RequestDTO.DTO_TYPE} which specified in root node if {@code RequestDTO.DTO_TYPE} exists, else {@code null}
   */
  default RequestDTO.DTO_TYPE getDTOType(Node root) {
    try {
      return RequestDTO.DTO_TYPE.valueOf(root.getNodeName());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  default String getDTOName(Node root) {
    return root.getAttributes().getNamedItem("name").getNodeValue();
  }

  RequestDTO.AbstractDTOConverter getConverter(RequestDTO.DTO_SECTION section);
}
