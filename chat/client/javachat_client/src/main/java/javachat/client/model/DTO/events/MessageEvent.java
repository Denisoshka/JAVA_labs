package javachat.client.model.DTO.events;

import javachat.client.model.DTO.DTOInterfaces;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageEvent extends EVENT_SECTION.Event implements DTOInterfaces.FROM, DTOInterfaces.MESSAGE {
  @XmlElement(name = "from", required = true)
  private String from;
  @XmlElement(name = "message", required = true)
  private String message;

  public MessageEvent(String from, String message) {
    super(EVENT_SECTION.MESSAGE.getName());
    this.from = from;
    this.message = message;
  }
}
