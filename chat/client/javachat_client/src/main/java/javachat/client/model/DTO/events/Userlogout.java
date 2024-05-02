package javachat.client.model.DTO.events;

import javachat.client.model.DTO.DTOInterfaces;

import java.util.Objects;


public class Userlogout extends Event implements DTOInterfaces.NAME {
  private String name;

  public Userlogout() {
    super(EVENTS.USER_LOGOUT);
  }

  public Userlogout(String name) {
    this();
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Userlogout that)) return false;
    if (!super.equals(o)) return false;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), name);
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
