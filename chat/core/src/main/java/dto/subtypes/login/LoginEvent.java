package dto.subtypes.login;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Objects;

@XmlRootElement(name = "event")
@XmlAccessorType(XmlAccessType.FIELD)
public  class LoginEvent implements DTOInterfaces.EVENT_DTO, DTOInterfaces.NAME {
  @XmlAttribute(name = "name")
  private final String nameAttribute = RequestDTO.EVENT_TYPE.USERLOGIN.getName();
  private String name;

  public LoginEvent() {
  }

  public LoginEvent(String name) {
    this.name = name;
  }

  @Override
  public RequestDTO.EVENT_TYPE getEventType() {
    return null;
  }

  @Override
  public String getNameAttribute() {
    return nameAttribute;
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return RequestDTO.DTO_SECTION.LOGIN;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof LoginEvent event)) return false;
    return Objects.equals(nameAttribute, event.nameAttribute) && Objects.equals(name, event.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nameAttribute, name);
  }
}
