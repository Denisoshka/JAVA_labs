package javachat.client.model.DTO.events;

import javachat.client.model.DTO.DTOInterfaces;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginEvent extends EVENT_SECTION.Event implements DTOInterfaces.NAME {
  private String name;

  public UserLoginEvent(String name) {
    super(EVENT_SECTION.USER_LOGIN.getName());
    this.name = name;
  }
}
