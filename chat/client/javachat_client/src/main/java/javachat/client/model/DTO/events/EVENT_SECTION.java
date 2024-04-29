package javachat.client.model.DTO.events;

import javachat.client.model.DTO.DTOInterfaces;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

public enum EVENT_SECTION {
  MESSAGE("message"),
  USER_LOGIN("userlogin"),
  USER_LOGOUT("userlogout"),
  ;

  private final String name;

  EVENT_SECTION(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Getter
  @Setter
  @EqualsAndHashCode(callSuper = false)
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlRootElement(name = "event")
  @XmlSeeAlso({MessageEvent.class, UserLoginEvent.class, UserLogoutEvent.class})
  public static class Event implements DTOInterfaces.NAME_ATTRIBUTE {
    @XmlAttribute(name = "name", required = true)
    private String nameAttribute;

    public Event(String name) {
      this.nameAttribute = name;
    }
  }
}
