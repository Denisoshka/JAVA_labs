package dto.subtypes.other;

import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Objects;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User implements DTOInterfaces.NAME, DTOInterfaces.CONTENT, DTOInterfaces.MIME_TYPE {
  private String name;
  private byte[] content;
  private String mimeType;

  public User() {
  }

  public User(String name) {
    this.name = name;
  }

  public User(String name, byte[] content, String mimeType) {
    this.name = name;
    this.content = content;
    this.mimeType = mimeType;
  }

  @XmlElement(name = "name")
  public String getName() {
    return this.name;
  }


  @Override
  public byte[] getContent() {
    return content;
  }

  @Override
  public String getMimeType() {
    return mimeType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User user)) return false;
    return Objects.equals(name, user.name);
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

}