package dto.subtypes.message;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.*;

import java.util.Objects;

@XmlType(name = "messagecommand")
@XmlRootElement(name = "command")
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageCommand implements DTOInterfaces.COMMAND_DTO, DTOInterfaces.MESSAGE {
  @XmlAttribute(name = "name")
  private final String nameAttribute = RequestDTO.EVENT_TYPE.MESSAGE.getName();
  private String message;

  public MessageCommand() {
  }

  public MessageCommand(String message) {
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
    if (!(o instanceof MessageCommand messageCommand)) return false;
    return Objects.equals(nameAttribute, messageCommand.nameAttribute) && Objects.equals(message, messageCommand.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nameAttribute, message);
  }
}