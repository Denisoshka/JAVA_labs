package javachat.client.model.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


public class RequestDTO {

  @Getter
  public record LoginData(String name, String hostname, String password, int port) implements DTOInterfaces.NAME,
          DTOInterfaces.HOSTNAME,
          DTOInterfaces.PASSWORD,
          DTOInterfaces.PORT {
  }

  @Setter
  @Getter
  @XmlRootElement(name = "name")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class User implements DTOInterfaces.NAME {
    @XmlElement(name = "name", required = true)
    private String name;

    public User(String name) {
      this.name = name;
    }
  }
}
