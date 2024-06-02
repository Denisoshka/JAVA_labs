package dto.subtypes.file;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Objects;

import static dto.RequestDTO.COMMAND_TYPE.LISTFILE;

@XmlRootElement(name = "command")
public  class ListFileCommand implements DTOInterfaces.COMMAND_DTO {
  @XmlAttribute(name = "name")
  private final String nameAttribute = RequestDTO.COMMAND_TYPE.LISTFILE.getName();

  @Override
  public String getNameAttribute() {
    return nameAttribute;
  }

  @Override
  public RequestDTO.COMMAND_TYPE getCommandType() {
    return LISTFILE;
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return LISTFILE.geDTOSection();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ListFileCommand that)) return false;
    return Objects.equals(nameAttribute, that.nameAttribute);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(nameAttribute);
  }
}
