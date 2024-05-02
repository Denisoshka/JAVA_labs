package javachat.client.model.DTO.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javachat.client.model.DTO.DTOInterfaces;

import java.util.Objects;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "name")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "message", value = Message.class),
        @JsonSubTypes.Type(name = "userlogin", value = Userlogin.class),
        @JsonSubTypes.Type(name = "userlogout", value = Userlogout.class)
})
@JacksonXmlRootElement(localName = "event")
public class Event implements DTOInterfaces.NAME_ATTRIBUTE {
  public enum EVENTS_TYPES {
    MESSAGE("message"),
    USER_LOGIN("userlogin"),

    USER_LOGOUT("userlogout"),
    ;

    private final String type;

    EVENTS_TYPES(String name) {
      this.type = name;
    }

    public String getType() {
      return type;
    }
  }

  @JacksonXmlProperty(localName = "name", isAttribute = true)
  private String nameattribute;

  public Event() {
  }

//  @JsonCreator
  public Event(/*@JsonProperty("name")*/ String name) {
    this.nameattribute = name;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Event event)) return false;
    return Objects.equals(nameattribute, event.nameattribute);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nameattribute);
  }

  public String getNameattribute() {
    return nameattribute;
  }

  public void setNameattribute(String nameattribute) {
    this.nameattribute = nameattribute;
  }
}
