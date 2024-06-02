package dto.subtypes.file;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Objects;

@XmlRootElement(name = "event")
@XmlAccessorType(XmlAccessType.FIELD)
public class FileEvent implements DTOInterfaces.EVENT_DTO, DTOInterfaces.ID, DTOInterfaces.FROM, DTOInterfaces.NAME, DTOInterfaces.SIZE, DTOInterfaces.MIME_TYPE {
  @XmlAttribute(name = "name")
  private final String nameAttribute = RequestDTO.EVENT_TYPE.FILE.getName();
  private Long id;
  private String from;
  private String name;
  private long size;
  private String mimeType;

  public FileEvent() {
  }

  public FileEvent(Long id, String from, String name, long size, String mimeType) {
    this.id = id;
    this.from = from;
    this.name = name;
    this.size = size;
    this.mimeType = mimeType;
  }

  @Override
  public RequestDTO.EVENT_TYPE getEventType() {
    return RequestDTO.EVENT_TYPE.FILE;
  }

  @Override
  public String getNameAttribute() {
    return RequestDTO.EVENT_TYPE.FILE.getName();
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return RequestDTO.EVENT_TYPE.FILE.geDTOSection();
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getFrom() {
    return from;
  }

  @Override
  public long getSize() {
    return size;
  }

  @Override
  public String getMimeType() {
    return mimeType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof FileEvent fileEvent)) return false;
    return size == fileEvent.size && Objects.equals(nameAttribute, fileEvent.nameAttribute) && Objects.equals(id, fileEvent.id) && Objects.equals(from, fileEvent.from) && Objects.equals(name, fileEvent.name) && Objects.equals(mimeType, fileEvent.mimeType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nameAttribute, id, from, name, size, mimeType);
  }
}
