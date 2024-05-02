package javachat.client.model.DTO.events;

import javachat.client.model.DTO.DTOInterfaces;

import java.util.Objects;

public class Userlogin extends Event implements DTOInterfaces.NAME {
  private String name;
  public Userlogin() {
    super(EVENTS.USER_LOGIN);
  }

  public Userlogin(String name) {
    this();
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Userlogin that)) return false;
    if (!super.equals(o)) return false;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), name);
  }
}
