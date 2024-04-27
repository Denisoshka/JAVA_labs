package javachat.client.model.DTO.Commands;

import javachat.client.model.DTO.DTOInterfaces;

import javax.xml.bind.annotation.*;
import java.util.Objects;

public enum CommandsTypes {
  MESSAGE("message"),
  LOGOUT("logout");

  CommandsTypes(String name) {
    this.type = name;
  }

  final String type;

  public String getType() {
    return this.type;
  }

  @XmlSeeAlso({Messages.class})
  @XmlRootElement(name = "command")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Command implements DTOInterfaces.NAME {
    @XmlAttribute(name = "name")
    private String name;

    public Command(String name) {
      this.name = name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public boolean equals(final Object o) {
      if (o == this) return true;
      if (!(o instanceof Command other)) return false;
      if (!other.canEqual(this)) return false;
      final Object this$name = this.getName();
      final Object other$name = other.getName();
      return Objects.equals(this$name, other$name);
    }

    protected boolean canEqual(final Object other) {
      return other instanceof Command;
    }

    public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      final Object $name = this.getName();
      result = result * PRIME + ($name == null ? 43 : $name.hashCode());
      return result;
    }

    public String toString() {
      return "RequestDTO.Commands.Command(name=" + this.getName() + ")";
    }

    public String getName() {
      return this.name;
    }
  }


}