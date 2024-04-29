package javachat.client.model.DTO.commands;

import javachat.client.model.DTO.DTOInterfaces;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

import static javachat.client.model.DTO.commands.COMMAND_SECTION.MESSAGE;


public class MessageCommand {
  @Data
  @EqualsAndHashCode(callSuper = true)
  @XmlAccessorType(XmlAccessType.FIELD)
  public static final class Message extends COMMAND_SECTION.Command implements DTOInterfaces.MESSAGE {
    @XmlElement(name = "message")
    private String message;

    public Message(String message) {
      super(MESSAGE.getType());
      this.message = message;
    }
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  @XmlSeeAlso({MessageSuccessResponse.class, MessageErrorResponse.class})
  public static class MessageResponse extends COMMAND_SECTION.Response {
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class MessageSuccessResponse extends COMMAND_SECTION.SuccessResponse  {
  }

  @Getter
  @Setter
  @EqualsAndHashCode(callSuper = true)
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class MessageErrorResponse extends COMMAND_SECTION.ErrorResponse {
    public MessageErrorResponse(String message) {
      super(message);
    }
  }
}
