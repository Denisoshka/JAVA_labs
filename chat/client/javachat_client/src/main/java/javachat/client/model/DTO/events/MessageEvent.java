package javachat.client.model.DTO.events;

import javachat.client.model.DTO.DTOInterfaces;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import javax.xml.bind.annotation.*;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "event")
@XmlType(name = "MessageEvent")
@XmlDiscriminatorValue("message")
public class MessageEvent extends EVENT_SECTION.Event implements DTOInterfaces.FROM, DTOInterfaces.MESSAGE {
  @XmlElement(name = "from", required = true)
  private String from;
  @XmlElement(name = "message", required = true)
  private String message;

  public MessageEvent() {
    super(EVENT_SECTION.MESSAGE.getType());
  }

  public MessageEvent(String from, String message) {
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

  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
