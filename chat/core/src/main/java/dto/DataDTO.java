package dto;

import dto.interfaces.DTOInterfaces;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;


public class DataDTO {
  public static class LoginData implements DTOInterfaces.NAME,
          DTOInterfaces.HOSTNAME,
          DTOInterfaces.PASSWORD,
          DTOInterfaces.PORT {
    String name;
    String hostname;
    String password;
    int port;

    public LoginData(String name, String hostname, String password, int port) {
      this.name = name;
      this.hostname = hostname;
      this.password = password;
      this.port = port;
    }

    public String getName() {
      return this.name;
    }

    public String getHostname() {
      return this.hostname;
    }

    public String getPassword() {
      return this.password;
    }

    public int getPort() {
      return this.port;
    }
  }

  @XmlRootElement(name = "user")
  public static class User implements DTOInterfaces.NAME {
    private String name;

    public User() {
    }

    public User(String name) {
      this.name = name;
    }

    @XmlElement(name = "name")
    public String getName() {
      return this.name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof User user)) return false;
      return Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name);
    }
  }
}
