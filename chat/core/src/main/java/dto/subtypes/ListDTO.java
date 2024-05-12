package dto.subtypes;

import dto.RequestDTO;
import dto.DataDTO;
import dto.interfaces.DTOInterfaces;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;
import java.util.List;

public enum ListDTO {
  ;

  public static class ListDTOConverter extends RequestDTO.DTOConverter {
    public ListDTOConverter() throws JAXBException {
      super(JAXBContext.newInstance(Command.class, Error.class, Success.class/*, XyiDTO.User.class*/));
    }
  }

  @XmlRootElement(name = "command")
  public static class Command extends RequestDTO.BaseCommand {
    public Command() {
      super(DTO_SECTION.LIST);
    }
  }

  @XmlRootElement(name = "success")
  @XmlAccessorType(XmlAccessType.NONE)
  public static class Success extends RequestDTO.BaseSuccessResponse implements DTOInterfaces.USERS {
    private List<DataDTO.User> users;

    public Success() {
      super(DTO_SECTION.LIST);
    }

    public Success(List<DataDTO.User> users) {
      super(DTO_SECTION.LIST);
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
  }

  @XmlRootElement(name = "error")
  public static class Error extends RequestDTO.BaseErrorResponse {
    public Error() {
      super(DTO_SECTION.LIST);
    }

    public Error(String message) {
      super(DTO_SECTION.LIST, message);
    }
  }
}
