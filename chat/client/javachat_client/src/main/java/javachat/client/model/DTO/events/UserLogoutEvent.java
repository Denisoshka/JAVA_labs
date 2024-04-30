package javachat.client.model.DTO.events;

import javachat.client.model.DTO.DTOInterfaces;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Objects;

@XmlRootElement(name = "event")
@XmlType(name = "UserLogoutEvent")
public class UserLogoutEvent extends EVENT_SECTION.Event implements DTOInterfaces.NAME {
  @XmlElement(name = "name", required = true)
  private String name;

  public UserLogoutEvent() {
    super(EVENT_SECTION.USER_LOGOUT.getType());
  }

  public UserLogoutEvent(String name) {
    this();
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof UserLogoutEvent that)) return false;
    if (!super.equals(o)) return false;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), name);
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
