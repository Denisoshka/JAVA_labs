package dto.subtypes.list;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Objects;

@XmlRootElement(name = "command")
public class ListCommand implements DTOInterfaces.COMMAND_DTO {
  @XmlAttribute(name = "name")
  private final String nameAttribute = RequestDTO.COMMAND_TYPE.LIST.getName();

  @Override
  public String getNameAttribute() {
    return nameAttribute;
  }

  @Override
  public RequestDTO.COMMAND_TYPE getCommandType() {
    return RequestDTO.COMMAND_TYPE.LIST;
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return RequestDTO.COMMAND_TYPE.LIST.geDTOSection();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ListCommand command)) return false;
    return Objects.equals(nameAttribute, command.nameAttribute);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(nameAttribute);
  }
}

