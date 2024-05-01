package javachat.client.model.DTO.events;

import javachat.client.model.DTO.DTOInterfaces;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Objects;

@XmlRootElement(name = "event")
@XmlType(name = "UserLoginEvent")
@XmlDiscriminatorValue("userlogin")
public class UserLoginEvent extends EVENT_SECTION.Event implements DTOInterfaces.NAME {
  @XmlElement(name = "name", required = true)
  private String name;

  public UserLoginEvent() {
    super(EVENT_SECTION.USER_LOGIN.getType());
  }

  public UserLoginEvent(String name) {
    this();
    this.name = name;
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
    if (!(o instanceof UserLoginEvent that)) return false;
    if (!super.equals(o)) return false;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), name);
  }
}
