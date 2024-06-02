package dto.subtypes.message;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.*;

import java.util.Objects;

@XmlRootElement(name = "event")
@XmlType(name = "messageevent")
@XmlAccessorType(XmlAccessType.FIELD)
public  class MessageEvent implements DTOInterfaces.EVENT_DTO, DTOInterfaces.FROM, DTOInterfaces.MESSAGE {
  @XmlAttribute(name = "name")
  private final String nameAttribute = RequestDTO.EVENT_TYPE.MESSAGE.getName();
  private String from;
  private String message;

  public MessageEvent() {
  }

  public MessageEvent(String from, String message) {
    this();
    this.from = from;
    this.message = message;
  }

  @Override
  public RequestDTO.EVENT_TYPE getEventType() {
    return RequestDTO.EVENT_TYPE.MESSAGE;
  }

  @Override
  public String getNameAttribute() {
    return nameAttribute;
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return RequestDTO.DTO_SECTION.MESSAGE;
  }

  @Override
  public String getFrom() {
    return from;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MessageEvent messageEvent)) return false;
    return Objects.equals(nameAttribute, messageEvent.nameAttribute) && Objects.equals(from, messageEvent.from) && Objects.equals(message, messageEvent.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nameAttribute, from, message);
  }
}
