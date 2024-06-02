package dto.subtypes.logout;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "logoutsuccess")
@XmlRootElement(name = "success")
@XmlAccessorType(XmlAccessType.FIELD)
public class LogoutSuccess implements DTOInterfaces.SUCCESS_RESPONSE_DTO {
  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return RequestDTO.DTO_SECTION.LOGOUT;
  }

  public boolean equals(final Object o) {
    if (o == this) return true;
    if (!(o instanceof LogoutSuccess)) return false;
    final LogoutSuccess other = (LogoutSuccess) o;
    if (!other.canEqual((Object) this)) return false;
    return true;
  }

  protected boolean canEqual(final Object other) {
    return other instanceof LogoutSuccess;
  }

  public int hashCode() {
    int result = 1;
    return result;
  }
}
