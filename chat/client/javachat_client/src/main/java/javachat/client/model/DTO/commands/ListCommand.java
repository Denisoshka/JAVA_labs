package javachat.client.model.DTO.commands;

import javachat.client.model.DTO.DTOInterfaces;
import javachat.client.model.DTO.RequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.*;
import java.util.List;

public class ListCommand extends COMMAND_SECTION.Command {

  public ListCommand() {
    super(COMMAND_SECTION.LIST.getType());
  }


  @XmlSeeAlso({MessageCommand.MessageSuccessResponse.class, MessageCommand.MessageErrorResponse.class})
  public static class ListResponse extends COMMAND_SECTION.Response {
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class ListSuccessResponse extends COMMAND_SECTION.SuccessResponse implements DTOInterfaces.USERS {
    @XmlElementWrapper(name = "users")
    @XmlElement(name = "user")
    private List<RequestDTO.User> users;

    public ListSuccessResponse(List<RequestDTO.User> users) {
      this.users = users;
    }

    public List<RequestDTO.User> getUsers() {
      return users;
    }
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  public static class ListErrorResponse extends COMMAND_SECTION.ErrorResponse {
    public ListErrorResponse(String message) {
      super(message);
    }
  }
}
