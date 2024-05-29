package dto.subtypes.file;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.*;

import java.util.Objects;

import static dto.RequestDTO.DTO_SECTION.FILE;

@XmlType(name = "fileerror")
@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.FIELD)
public class FileError implements DTOInterfaces.ERROR_RESPONSE_DTO {
  private String message;

  public FileError() {
  }

  public FileError(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return FILE;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof FileError error)) return false;
    return Objects.equals(message, error.message);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(message);
  }
}
