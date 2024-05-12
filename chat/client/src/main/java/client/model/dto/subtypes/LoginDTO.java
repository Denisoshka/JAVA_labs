package client.model.dto.subtypes;

import javachat.client.model.dto.RequestDTO;
import javachat.client.model.dto.interfaces.DTOInterfaces;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

public enum LoginDTO {
  ;

  public static class LoginDTOConverter extends RequestDTO.DTOConverter {
    public LoginDTOConverter() throws JAXBException {
      super(JAXBContext.newInstance(Command.class, Event.class, Error.class, Success.class));
    }
  }

  @XmlRootElement(name = "command")
  public static class Command extends RequestDTO.BaseCommand implements DTOInterfaces.NAME, DTOInterfaces.PASSWORD {
    private String name;
    private String password;

    public Command() {
      super(DTO_SECTION.LOGIN);
    }

    public Command(String name, String password) {
      this();
      this.name = name;
      this.password = password;
    }

    @Override
    @XmlElement(name = "name")
    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Override
    @XmlElement(name = "password")
    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Command command)) return false;
      if (!super.equals(o)) return false;
      return Objects.equals(name, command.name) && Objects.equals(password, command.password);
    }

    @Override
    public int hashCode() {
      return Objects.hash(super.hashCode(), name, password);
    }
  }

  @XmlRootElement(name = "event")
  public static class Event extends RequestDTO.BaseEvent implements DTOInterfaces.NAME {
    private String name;

    Event() {
      super(EVENT_TYPE.USER_LOGIN, DTO_SECTION.LOGIN);
    }

    public Event(String name) {
      this();
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

    @Override
    @XmlElement(name = "name")
    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  @XmlRootElement(name = "success")
  public static class Success extends RequestDTO.BaseSuccessResponse {
    public Success() {
      super(DTO_SECTION.LOGIN);
    }
  }

  @XmlRootElement(name = "error")
  public static class Error extends RequestDTO.BaseErrorResponse {
    public Error() {
      super(DTO_SECTION.LOGIN);
    }

    public Error(String message) {
      super(DTO_SECTION.LOGIN, message);
    }
  }
}
