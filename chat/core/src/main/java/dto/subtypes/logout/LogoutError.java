package dto.subtypes.logout;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.Objects;

@XmlType(name = "logouterror")
@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.FIELD)
public class LogoutError implements DTOInterfaces.ERROR_RESPONSE_DTO {
  private String message;

  public LogoutError() {
  }

  public LogoutError(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return RequestDTO.DTO_SECTION.LOGOUT;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof LogoutError logoutError)) return false;
    return Objects.equals(message, logoutError.message);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(message);
  }
}
