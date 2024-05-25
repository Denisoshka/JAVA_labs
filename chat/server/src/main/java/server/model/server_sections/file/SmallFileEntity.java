package server.model.server_sections.file;

import jakarta.persistence.*;

@Entity
@Table(name = "FILES")
public class SmallFileEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "file_name", nullable = false)
  private String fileName;
  @Column(name = "user_name", nullable = false)
  private String userName;
  @Column(name = "mime_type")
  private String mimeType;
  @Column(name = "file_size", nullable = false)
  private long size;
  @Column(name = "file_content", nullable = false)
  private byte[] content;

  public SmallFileEntity(String fileName, String userName, String mimeType, long size, byte[] content) {
    this.fileName = fileName;
    this.userName = userName;
    this.mimeType = mimeType;
    this.size = size;
    this.content = content;
  }

  public SmallFileEntity() {
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public byte[] getContent() {
    return content;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }
}
