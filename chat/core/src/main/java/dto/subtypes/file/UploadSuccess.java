package dto.subtypes.file;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.Objects;

import static dto.RequestDTO.DTO_SECTION.FILE;

@XmlType(name = "uploadsuccess")
@XmlRootElement(name = "success")
@XmlAccessorType(XmlAccessType.FIELD)
public class UploadSuccess implements DTOInterfaces.SUCCESS_RESPONSE_DTO, DTOInterfaces.ID {
  private Long id;

  public UploadSuccess() {
  }

  public UploadSuccess(Long id) {
    this.id = id;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return FILE;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof UploadSuccess that)) return false;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
