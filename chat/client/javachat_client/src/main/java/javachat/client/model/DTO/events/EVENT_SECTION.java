package javachat.client.model.DTO.events;

import javachat.client.model.DTO.DTOInterfaces;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorNode;

import javax.xml.bind.annotation.*;
import java.util.Objects;

public enum EVENT_SECTION implements DTOInterfaces.TYPE {
  MESSAGE("message"),
  USER_LOGIN("userlogin"),
  USER_LOGOUT("userlogout"),
  ;

  private final String type;

  EVENT_SECTION(String name) {
    this.type = name;
  }

  public String getType() {
    return type;
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "BaseEvent")
  @XmlDiscriminatorNode("@nameAttribute")
  @XmlSeeAlso({MessageEvent.class, UserLoginEvent.class, UserLogoutEvent.class})
  public static class Event implements DTOInterfaces.NAME_ATTRIBUTE {
    @XmlAttribute(name = "name", required = true)
    protected String nameAttribute;

    public Event() {
    }

    public Event(String name) {
      this.nameAttribute = name;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Event event)) return false;
      return Objects.equals(nameAttribute, event.nameAttribute);
    }

    @Override
    public int hashCode() {
      return Objects.hash(nameAttribute);
    }

    public String getNameAttribute() {
      return nameAttribute;
    }

    public void setNameAttribute(String nameAttribute) {
      this.nameAttribute = nameAttribute;
    }
  }
}
