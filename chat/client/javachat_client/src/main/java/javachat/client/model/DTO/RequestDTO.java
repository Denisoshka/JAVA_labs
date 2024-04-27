package javachat.client.model.DTO;

import lombok.Getter;

import javax.xml.bind.annotation.*;
import java.util.Objects;


public class RequestDTO {

  @Getter
  public record LoginData(String name, String hostname, String password, int port) implements DTOInterfaces.NAME,
          DTOInterfaces.HOSTNAME,
          DTOInterfaces.PASSWORD,
          DTOInterfaces.PORT {
  }

  public static class Root {
    String root;
  }

  public static class Events {
    ;

  }


}
