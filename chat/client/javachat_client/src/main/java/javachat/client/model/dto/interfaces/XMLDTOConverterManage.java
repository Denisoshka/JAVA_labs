package javachat.client.model.dto.interfaces;

import javachat.client.model.dto.RequestDTO;
import org.w3c.dom.Node;

public interface XMLDTOConverterManage {
  default RequestDTO.DTO_TYPE getDTOType(Node root) throws IllegalArgumentException {
    return RequestDTO.DTO_TYPE.valueOf(root.getNodeName());
  }

  default String getDTOName(Node root) {
    return root.getAttributes().getNamedItem("name").getNodeValue();
  }

  public RequestDTO.DTOConverter getConverter(RequestDTO.DTO_SECTION section);
}
