package javachat.client.model.DTO.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javachat.client.model.DTO.DTOInterfaces;

import java.util.Objects;

public class MessageEvent extends Event implements DTOInterfaces.FROM, DTOInterfaces.MESSAGE {
  private String from;
  private String message;

  public MessageEvent() {
    super(EVENTS.MESSAGE);
  }

  @JsonCreator
  public MessageEvent(@JsonProperty("from") String from, @JsonProperty("message") String message) {
    this();
    this.from = from;
    this.message = message;
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MessageEvent that)) return false;
    if (!super.equals(o)) return false;
    return Objects.equals(from, that.from) && Objects.equals(message, that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), from, message);
  }
    @Override
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
