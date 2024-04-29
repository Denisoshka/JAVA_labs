package javachat.client.model.DTO;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


public class RequestDTO {

  @Getter
  public static class LoginData implements DTOInterfaces.NAME,
          DTOInterfaces.HOSTNAME,
          DTOInterfaces.PASSWORD,
          DTOInterfaces.PORT {
    String name;
    String hostname;
    String password;
    int port;

    LoginData(String name, String hostname, String password, int port) {
      this.name = name;
      this.hostname = hostname;
      this.password = password;
      this.port = port;
    }
  }

  @XmlRootElement(name = "name")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class User implements DTOInterfaces.NAME {
    @XmlElement(name = "name", required = true)
    private String name;

    public User() {
    }

    public User(String name) {
      this.name = name;
    }

    public String getName() {
      return this.name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String toString() {
      return "RequestDTO.User(name=" + this.getName() + ")";
    }

    public boolean equals(final Object o) {
      if (o == this) return true;
      if (!(o instanceof User)) return false;
      final User other = (User) o;
      if (!other.canEqual((Object) this)) return false;
      final Object this$name = this.getName();
      final Object other$name = other.getName();
      if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
      return true;
    }

    protected boolean canEqual(final Object other) {
      return other instanceof User;
    }

    public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      final Object $name = this.getName();
      result = result * PRIME + ($name == null ? 43 : $name.hashCode());
      return result;
    }
  }
}
