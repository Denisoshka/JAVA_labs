package dto.subtypes.file;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@XmlRootElement(name = "success")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListFileSuccess implements DTOInterfaces.SUCCESS_RESPONSE_DTO, DTOInterfaces.FILES {
  @XmlElementWrapper(name = "files")
  @XmlElement(name = "file")
  private List<FileEntity> files;

  public ListFileSuccess() {
  }

  public ListFileSuccess(List<FileEntity> files) {
    this.files = files;
  }

  public List<FileEntity> getFiles() {
    return files;
  }

  public void setFiles(List<FileEntity> files) {
    this.files = files;
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return RequestDTO.DTO_SECTION.FILE;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ListFileSuccess that)) return false;
    return Objects.equals(files, that.files);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(files);
  }
}

