package dto.subtypes.file;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.*;

import java.util.Objects;

import static dto.RequestDTO.COMMAND_TYPE.DOWNLOAD;

@XmlType(name = "ownloadfilecommand")
@XmlRootElement(name = "command")
@XmlAccessorType(XmlAccessType.FIELD)
public class DownloadCommand implements DTOInterfaces.COMMAND_DTO, DTOInterfaces.ID {
  @XmlAttribute(name = "name")
  private final String nameAttribute = DOWNLOAD.getName();
  private Long id;

  public DownloadCommand() {
  }

  public DownloadCommand(Long id) {
    this.id = id;
  }

  @Override
  public String getNameAttribute() {
    return nameAttribute;
  }

  @Override
  public RequestDTO.COMMAND_TYPE getCommandType() {
    return DOWNLOAD;
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return DOWNLOAD.geDTOSection();
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof DownloadCommand command)) return false;
    return Objects.equals(nameAttribute, command.nameAttribute) && Objects.equals(id, command.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nameAttribute, id);
  }
}
