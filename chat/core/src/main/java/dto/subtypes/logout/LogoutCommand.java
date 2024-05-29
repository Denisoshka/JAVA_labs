package dto.subtypes.logout;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.*;

import java.util.Objects;

@XmlType(name = "logoutcommand")
@XmlRootElement(name = "command")
@XmlAccessorType(XmlAccessType.FIELD)
public class LogoutCommand implements DTOInterfaces.COMMAND_DTO {
  @XmlAttribute(name = "name")
  private final String nameAttribute = RequestDTO.COMMAND_TYPE.LOGOUT.getName();

  @Override
  public String getNameAttribute() {
    return nameAttribute;
  }

  @Override
  public RequestDTO.COMMAND_TYPE getCommandType() {
    return RequestDTO.COMMAND_TYPE.LOGOUT;
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return RequestDTO.DTO_SECTION.LOGOUT;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof LogoutCommand logoutCommand)) return false;
    return Objects.equals(nameAttribute, logoutCommand.nameAttribute);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(nameAttribute);
  }
}

