package dto.subtypes;

import dto.BaseDTOConverter;
import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.annotation.*;

import java.util.Objects;

public enum MessageDTO {
  ;

  public static class MessageDTOConverter extends BaseDTOConverter {
    public MessageDTOConverter() throws JAXBException {
      super(JAXBContext.newInstance(Command.class, Event.class, Error.class, Success.class));
    }
  }

  @XmlType(name = "messagecommand")
  @XmlRootElement(name = "command")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Command implements DTOInterfaces.COMMAND_DTO, DTOInterfaces.MESSAGE {
    @XmlAttribute(name = "name")
    private final String nameAttribute = RequestDTO.EVENT_TYPE.MESSAGE.getName();
    private String message;

    public Command() {
    }

    public Command(String message) {
      this.message = message;
    }

    @Override
    public String getNameAttribute() {
      return nameAttribute;
    }

    @Override
    public RequestDTO.COMMAND_TYPE getCommandType() {
      return RequestDTO.COMMAND_TYPE.MESSAGE;
    }

    @Override
    public RequestDTO.DTO_SECTION getDTOSection() {
      return RequestDTO.DTO_SECTION.MESSAGE;
    }

    @Override
    public String getMessage() {
      return message;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Command command)) return false;
      return Objects.equals(nameAttribute, command.nameAttribute) && Objects.equals(message, command.message);
    }

    @Override
    public int hashCode() {
      return Objects.hash(nameAttribute, message);
    }
  }

  @XmlRootElement(name = "event")
  @XmlType(name = "messageevent")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Event implements DTOInterfaces.EVENT_DTO, DTOInterfaces.FROM, DTOInterfaces.MESSAGE {
    @XmlAttribute(name = "name")
    private final String nameAttribute = RequestDTO.EVENT_TYPE.MESSAGE.getName();
    private String from;
    private String message;

    public Event() {
    }

    public Event(String from, String message) {
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
      if (!(o instanceof Event event)) return false;
      return Objects.equals(nameAttribute, event.nameAttribute) && Objects.equals(from, event.from) && Objects.equals(message, event.message);
    }

    @Override
    public int hashCode() {
      return Objects.hash(nameAttribute, from, message);
    }
  }

  @XmlRootElement(name = "success")
  public static class Success implements DTOInterfaces.SUCCESS_RESPONSE_DTO {
    @Override
    public RequestDTO.DTO_SECTION getDTOSection() {
      return RequestDTO.DTO_SECTION.MESSAGE;
    }

    public boolean equals(final Object o) {
      if (o == this) return true;
      if (!(o instanceof Success)) return false;
      final Success other = (Success) o;
      if (!other.canEqual((Object) this)) return false;
      return true;
    }

    protected boolean canEqual(final Object other) {
      return other instanceof Success;
    }

    public int hashCode() {
      int result = 1;
      return result;
    }
  }

  @XmlRootElement(name = "error")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Error implements DTOInterfaces.ERROR_RESPONSE_DTO {
    private String message;

    public Error() {
    }

    public Error(String message) {
      this.message = message;
    }

    @Override
    public String getMessage() {
      return message;
    }

    @Override
    public RequestDTO.DTO_SECTION getDTOSection() {
      return RequestDTO.DTO_SECTION.MESSAGE;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Error error)) return false;
      return Objects.equals(message, error.message);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(message);
    }
  }
}
