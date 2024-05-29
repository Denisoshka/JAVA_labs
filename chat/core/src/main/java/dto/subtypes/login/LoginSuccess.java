package dto.subtypes.login;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "success")
public  class LoginSuccess implements DTOInterfaces.SUCCESS_RESPONSE_DTO {

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return RequestDTO.DTO_SECTION.LOGIN;
  }

  public boolean equals(final Object o) {
    if (o == this) return true;
    if (!(o instanceof LoginSuccess)) return false;
    final LoginSuccess other = (LoginSuccess) o;
    if (!other.canEqual((Object) this)) return false;
    return true;
  }

  protected boolean canEqual(final Object other) {
    return other instanceof LoginSuccess;
  }

  public int hashCode() {
    return 1;
  }
}
