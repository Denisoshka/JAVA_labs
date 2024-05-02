package javachat.client.model.DTO.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javachat.client.model.DTO.DTOInterfaces;

import java.util.Objects;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "name")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "base", value = Event.class),
        @JsonSubTypes.Type(name = "message", value = MessageEvent.class),
        @JsonSubTypes.Type(name = "userlogin", value = Userlogin.class),
        @JsonSubTypes.Type(name = "userlogout", value = Userlogout.class)
})
@JacksonXmlRootElement(localName = "event")
public class Event  {
  public enum EVENTS {
    BASE("base"),
    MESSAGE("message"),
    USER_LOGIN("userlogin"),
    USER_LOGOUT("userlogout"),
    ;

    private final String type;

    EVENTS(String name) {
      this.type = name;
    }

    public String getType() {
      return type;
    }
  }

//  @JacksonXmlProperty(localName = "name", isAttribute = true)
  @JsonIgnore
  private EVENTS type;
  public Event() {
//    this.nameattribute = EVENTS_TYPES.BASE.getType();
  }

  //  @JsonCreator
  public Event(/*@JsonProperty("name")*/ EVENTS name) {
//    this.nameattribute = name;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Event event)) return false;
    return Objects.equals(type, event.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type);
  }

  public EVENTS getType() {
    return type;
  }

  public void setNameattribute(EVENTS type) {
    this.type = type;
  }
}
