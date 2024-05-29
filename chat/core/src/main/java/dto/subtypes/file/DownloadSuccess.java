package dto.subtypes.file;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.Arrays;
import java.util.Objects;

import static dto.RequestDTO.DTO_SECTION.FILE;

@XmlType(name = "downloadsuccess"/*, propOrder = {"id", "name", "mimeType", "encoding", "content"}*/)
@XmlRootElement(name = "success")
@XmlAccessorType(XmlAccessType.FIELD)
public class DownloadSuccess implements DTOInterfaces.SUCCESS_RESPONSE_DTO, DTOInterfaces.ID, DTOInterfaces.NAME, DTOInterfaces.MIME_TYPE, DTOInterfaces.ENCODING, DTOInterfaces.CONTENT {
  private Long id;
  private String name;
  private String mimeType;
  private String encoding;
  private byte[] content;

  public DownloadSuccess() {
  }

  public DownloadSuccess(Long id, String name, String mimeType, String encoding, byte[] content) {
    this.id = id;
    this.name = name;
    this.mimeType = mimeType;
    this.encoding = encoding;
    this.content = content;
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return FILE;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public byte[] getContent() {
    return content;
  }

  @Override
  public String getEncoding() {
    return encoding;
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof DownloadSuccess that)) return false;
    return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(mimeType, that.mimeType) && Objects.equals(encoding, that.encoding) && Objects.deepEquals(content, that.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, mimeType, encoding, Arrays.hashCode(content));
  }
}
