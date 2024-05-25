package dto.subtypes;

import dto.BaseDTOConverter;
import dto.DataDTO;
import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static dto.RequestDTO.COMMAND_TYPE.LIST;

public enum ListDTO {
  ;

  public static class ListDTOConverter extends BaseDTOConverter {
    public ListDTOConverter() throws JAXBException {
      super(JAXBContext.newInstance(Command.class, Error.class, Success.class));
    }
  }

  @XmlRootElement(name = "command")
  public static class Command implements DTOInterfaces.COMMAND_DTO {
    @XmlAttribute(name = "name")
    private final String nameAttribute = LIST.getName();

    @Override
    public String getNameAttribute() {
      return nameAttribute;
    }

    @Override
    public RequestDTO.COMMAND_TYPE getCommandType() {
      return LIST;
    }

    @Override
    public RequestDTO.DTO_SECTION getDTOSection() {
      return LIST.geDTOSection();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Command command)) return false;
      return Objects.equals(nameAttribute, command.nameAttribute);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(nameAttribute);
    }
  }

  @XmlRootElement(name = "success")
  public static class Success implements DTOInterfaces.SUCCESS_RESPONSE_DTO, DTOInterfaces.USERS {
    private List<DataDTO.User> users;

    public Success() {
    }

    public Success(List<DataDTO.User> users) {
      this.users = users;
    }

    @Override
    @XmlElementWrapper(name = "users")
    @XmlElement(name = "user")
    public List<DataDTO.User> getUsers() {
      return users;
    }

    public void setUsers(List<DataDTO.User> users) {
      this.users = users;
    }

    @Override
    public RequestDTO.DTO_SECTION getDTOSection() {
      return RequestDTO.DTO_SECTION.LIST;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Success success)) return false;
      return Objects.equals(users, success.users);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(users);
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
      return RequestDTO.DTO_SECTION.LIST;
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
