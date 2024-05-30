package dto.subtypes.login;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Arrays;
import java.util.Objects;

@XmlRootElement(name = "success")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginSuccess implements DTOInterfaces.SUCCESS_RESPONSE_DTO, DTOInterfaces.CONTENT, DTOInterfaces.MIME_TYPE {
  private byte[] content;
  private String mimeType;

  public LoginSuccess(byte[] content, String mimeType) {
    this.content = content;
    this.mimeType = mimeType;
  }

  public LoginSuccess() {
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return RequestDTO.DTO_SECTION.LOGIN;
  }

  @Override
  public byte[] getContent() {
    return content;
  }

  @Override
  public String getMimeType() {
    return mimeType;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof LoginSuccess that)) return false;
    return Objects.deepEquals(content, that.content) && Objects.equals(mimeType, that.mimeType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(Arrays.hashCode(content), mimeType);
  }
}
