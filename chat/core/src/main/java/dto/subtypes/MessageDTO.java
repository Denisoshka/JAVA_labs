package dto.subtypes;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.Objects;

public enum MessageDTO {
  ;

  public static class MessageDTOConverter extends RequestDTO.BaseDTOConverter {
    public MessageDTOConverter() throws JAXBException {
      super(JAXBContext.newInstance(Command.class, Event.class, Error.class, Success.class));
    }
  }

  @XmlType(name = "messagecommand")
  @XmlRootElement(name = "command")
  public static class Command extends RequestDTO.BaseCommand implements DTOInterfaces.MESSAGE {
    private String message;

    public Command() {
      super(DTO_SECTION.MESSAGE, COMMAND_TYPE.MESSAGE);
    }

    public Command(String message) {
      this();
      this.message = message;
    }

    @Override
    @XmlElement(name = "message")
    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Command command)) return false;
      return Objects.equals(message, command.message);
    }

    @Override
    public int hashCode() {
      return Objects.hash(message);
    }
  }

  @XmlRootElement(name = "event")
  @XmlType(name = "messageevent")
  public static class Event extends RequestDTO.BaseEvent implements DTOInterfaces.FROM, DTOInterfaces.MESSAGE {
    private String from;
    private String message;

    public Event() {
      super(EVENT_TYPE.MESSAGE, DTO_SECTION.MESSAGE);
    }

    public Event(String from, String message) {
      this();
      this.from = from;
      this.message = message;
    }

    @Override
    @XmlElement(name = "from")
    public String getFrom() {
      return from;
    }

    public void setFrom(String from) {
      this.from = from;
    }

    @Override
    @XmlElement(name = "message")
    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Event event)) return false;
      return Objects.equals(from, event.from) && Objects.equals(message, event.message);
    }

    @Override
    public int hashCode() {
      return Objects.hash(from, message);
    }
  }

  @XmlRootElement(name = "success")
  public static class Success extends RequestDTO.BaseSuccessResponse {
    public Success() {
      super(DTO_SECTION.MESSAGE);
    }
  }

  @XmlRootElement(name = "error")
  public static class Error extends RequestDTO.BaseErrorResponse {
    public Error() {
      super(DTO_SECTION.MESSAGE);
    }

    public Error(String message) {
      super(DTO_SECTION.MESSAGE, message);
    }
  }
}
