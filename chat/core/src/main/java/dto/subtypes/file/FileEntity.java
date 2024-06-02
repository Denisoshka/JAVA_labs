package dto.subtypes.file;

import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.Objects;

@XmlType(name = "fileentity")
@XmlRootElement(name = "file")
@XmlAccessorType(XmlAccessType.FIELD)
public class FileEntity implements DTOInterfaces.ID, DTOInterfaces.FROM, DTOInterfaces.NAME, DTOInterfaces.SIZE, DTOInterfaces.MIME_TYPE {
  private Long id;
  private String from;
  private String name;
  private long size;
  private String mimeType;

  public FileEntity(Long id, String from, String name, long size, String mimeType) {
    this.id = id;
    this.from = from;
    this.name = name;
    this.size = size;
    this.mimeType = mimeType;
  }

  public FileEntity() {
  }

  @Override
  public String getFrom() {
    return from;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public String getMimeType() {
    return mimeType;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public long getSize() {
    return size;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof FileEntity that)) return false;
    return size == that.size && Objects.equals(id, that.id) && Objects.equals(from, that.from) && Objects.equals(name, that.name) && Objects.equals(mimeType, that.mimeType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, from, name, size, mimeType);
  }
}