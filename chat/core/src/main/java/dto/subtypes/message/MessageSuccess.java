package dto.subtypes.message;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "success")
public class MessageSuccess implements DTOInterfaces.SUCCESS_RESPONSE_DTO {
  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return RequestDTO.DTO_SECTION.MESSAGE;
  }

  public boolean equals(final Object o) {
    if (o == this) return true;
    if (!(o instanceof MessageSuccess other)) return false;
    return other.canEqual((Object) this);
  }

  protected boolean canEqual(final Object other) {
    return other instanceof MessageSuccess;
  }

  public int hashCode() {
    int result = 1;
    return result;
  }
}
