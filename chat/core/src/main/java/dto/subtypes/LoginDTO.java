package dto.subtypes;

import dto.BaseDTOConverter;
import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Objects;

public enum LoginDTO {
  ;

  public static class LoginDTOConverter extends BaseDTOConverter {
    public LoginDTOConverter() throws JAXBException {
      super(JAXBContext.newInstance(Command.class, Event.class, Error.class, Success.class));
    }
  }

  @XmlRootElement(name = "command")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Command implements DTOInterfaces.COMMAND_DTO, DTOInterfaces.NAME, DTOInterfaces.PASSWORD {
    @XmlAttribute(name = "name")
    private final String nameAttribute = RequestDTO.COMMAND_TYPE.LOGIN.getName();
    private String name;
    private String password;

    public Command() {
    }

    public Command(String name, String password) {
      this.name = name;
      this.password = password;
    }

    @Override
    public String getNameAttribute() {
      return nameAttribute;
    }

    @Override
    public RequestDTO.COMMAND_TYPE getCommandType() {
      return RequestDTO.COMMAND_TYPE.LOGIN;
    }

    @Override
    public RequestDTO.DTO_SECTION getDTOSection() {
      return RequestDTO.DTO_SECTION.LOGIN;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public String getPassword() {
      return password;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Command command)) return false;
      return Objects.equals(nameAttribute, command.nameAttribute) && Objects.equals(name, command.name) && Objects.equals(password, command.password);
    }

    @Override
    public int hashCode() {
      return Objects.hash(nameAttribute, name, password);
    }
  }

  @XmlRootElement(name = "event")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Event implements DTOInterfaces.EVENT_DTO, DTOInterfaces.NAME {
    @XmlAttribute(name = "name")
    private final String nameAttribute = RequestDTO.EVENT_TYPE.USERLOGIN.getName();
    private String name;

    public Event() {
    }

    public Event(String name) {
      this.name = name;
    }

    @Override
    public RequestDTO.EVENT_TYPE getEventType() {
      return null;
    }

    @Override
    public String getNameAttribute() {
      return nameAttribute;
    }

    @Override
    public RequestDTO.DTO_SECTION getDTOSection() {
      return RequestDTO.DTO_SECTION.LOGIN;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Event event)) return false;
      return Objects.equals(nameAttribute, event.nameAttribute) && Objects.equals(name, event.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(nameAttribute, name);
    }
  }

  @XmlRootElement(name = "success")
  public static class Success implements DTOInterfaces.SUCCESS_RESPONSE_DTO {

    @Override
    public RequestDTO.DTO_SECTION getDTOSection() {
      return RequestDTO.DTO_SECTION.LOGIN;
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
      return RequestDTO.DTO_SECTION.LOGIN;
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
