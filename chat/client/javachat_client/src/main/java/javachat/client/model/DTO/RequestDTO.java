package javachat.client.model.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;

import java.util.Objects;


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

  @JacksonXmlRootElement(localName = "event")
  public static class User implements DTOInterfaces.NAME {
    private String name;
    /*
    public User() {
    }
    */

    @JsonCreator
    public User(@JsonProperty("name") String name) {
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
