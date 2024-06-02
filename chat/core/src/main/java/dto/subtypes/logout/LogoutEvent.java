package dto.subtypes.logout;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.*;

import java.util.Objects;

@XmlType(name = "logoutevent")
@XmlRootElement(name = "event")
@XmlAccessorType(XmlAccessType.FIELD)
public class LogoutEvent implements DTOInterfaces.EVENT_DTO, DTOInterfaces.NAME {
  @XmlAttribute(name = "name")
  private final String nameAttribute = RequestDTO.EVENT_TYPE.USERLOGOUT.getName();
  String name;

  LogoutEvent() {
  }

  public LogoutEvent(String name) {
    this.name = name;
  }

  @Override
  public RequestDTO.EVENT_TYPE getEventType() {
    return RequestDTO.EVENT_TYPE.USERLOGOUT;
  }

  @Override
  public String getNameAttribute() {
    return nameAttribute;
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return RequestDTO.EVENT_TYPE.USERLOGOUT.geDTOSection();
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof LogoutEvent logoutEvent)) return false;
    return Objects.equals(nameAttribute, logoutEvent.nameAttribute) && Objects.equals(name, logoutEvent.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nameAttribute, name);
  }
}
