package dto.subtypes.message;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Objects;

@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageError implements DTOInterfaces.ERROR_RESPONSE_DTO {
  private String message;

  public MessageError() {
  }

  public MessageError(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return RequestDTO.DTO_SECTION.MESSAGE;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MessageError error)) return false;
    return Objects.equals(message, error.message);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(message);
  }
}
