package javachat.client.model.DTO.subtypes;

import javachat.client.model.DTO.RequestDTO;
import javachat.client.model.DTO.DTOInterfaces;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;
import java.util.Objects;

public enum LogoutDTO {;
  public static class LogoutDTOConverter extends RequestDTO.DTOConverter {
    public LogoutDTOConverter() throws JAXBException {
      super(JAXBContext.newInstance(Command.class, Event.class, Error.class, Success.class));
    }
  }

  @XmlType(name = "logoutcommand")
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlRootElement(name = "command")
  public static class Command extends RequestDTO.BaseCommand {
    public Command() {
      super(COMMAND_TYPE.LOGOUT);
    }
  }

  @XmlType(name = "logoutevent")
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlRootElement(name = "event")
  public static class Event extends RequestDTO.BaseEvent implements DTOInterfaces.NAME {
    @XmlElement(name = "name")
    String name;

    Event() {
      super(EVENT_TYPE.USER_LOGOUT);
    }

    public Event(String name) {
      this();
      this.name = name;
    }

    @Override
    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Event event)) return false;
      return Objects.equals(name, event.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name);
    }
  }

  @XmlType(name = "logoutsuccess")

  @XmlRootElement(name = "success")
  public static class Success extends RequestDTO.BaseSuccessResponse {
  }

  @XmlType(name = "logouterror")
  @XmlRootElement(name = "error")
  public static class Error extends RequestDTO.BaseErrorResponse {
    public Error() {
    }

    public Error(String message) {
      super(message);
    }
  }
}
