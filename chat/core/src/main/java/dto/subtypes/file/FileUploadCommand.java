package dto.subtypes.file;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.*;

import java.util.Arrays;
import java.util.Objects;

import static dto.RequestDTO.COMMAND_TYPE.UPLOAD;

@XmlType(name = "uploadfilecommand"/*, propOrder = {"name", "mimeType", "encoding", "content"}*/)
@XmlRootElement(name = "command")
@XmlAccessorType(XmlAccessType.FIELD)
public class FileUploadCommand implements DTOInterfaces.COMMAND_DTO, DTOInterfaces.NAME, DTOInterfaces.MIME_TYPE, DTOInterfaces.ENCODING, DTOInterfaces.CONTENT {
  @XmlAttribute(name = "name")
  private final String nameAttribute = UPLOAD.getName();
  private String name;
  private String mimeType;
  private String encoding;
  private byte[] content;

  public FileUploadCommand() {
  }

  public FileUploadCommand(String name, String mimeType, String encoding, byte[] content) {
    this.name = name;
    this.mimeType = mimeType;
    this.encoding = encoding;
    this.content = content;
  }

  @Override
  public String getNameAttribute() {
    return nameAttribute;
  }

  @Override
  public RequestDTO.COMMAND_TYPE getCommandType() {
    return UPLOAD;
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return UPLOAD.geDTOSection();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getMimeType() {
    return mimeType;
  }

  @Override
  public String getEncoding() {
    return encoding;
  }

  @Override
  public byte[] getContent() {
    return content;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof FileUploadCommand that)) return false;
    return Objects.equals(nameAttribute, that.nameAttribute) && Objects.equals(name, that.name) && Objects.equals(mimeType, that.mimeType) && Objects.equals(encoding, that.encoding) && Objects.deepEquals(content, that.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nameAttribute, name, mimeType, encoding, Arrays.hashCode(content));
  }
}