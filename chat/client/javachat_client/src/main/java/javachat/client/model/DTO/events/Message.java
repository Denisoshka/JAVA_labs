package javachat.client.model.DTO.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javachat.client.model.DTO.DTOInterfaces;

import java.util.Objects;

public class Message extends Event implements DTOInterfaces.FROM, DTOInterfaces.MESSAGE {
  @JacksonXmlProperty(localName = "from")
  private String from;
  @JacksonXmlProperty(localName = "message")
  private String message;

  public Message() {
    super(Event.EVENTS_TYPES.MESSAGE.getType());
  }

//  @JsonCreator
  public Message(/*@JsonProperty("from")*/ String from, /*@JsonProperty("message")*/ String message) {
    this();
//    super(Event.EVENTS_TYPES.MESSAGE.getType());
    this.from = from;
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Message that)) return false;
    if (!super.equals(o)) return false;
    return Objects.equals(from, that.from) && Objects.equals(message, that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), from, message);
  }

  //  @Override
  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  //  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
