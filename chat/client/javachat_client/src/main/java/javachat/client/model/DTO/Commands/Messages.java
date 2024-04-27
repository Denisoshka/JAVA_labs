package javachat.client.model.DTO.Commands;

import javachat.client.model.DTO.DTOInterfaces;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Objects;

import static javachat.client.model.DTO.Commands.CommandsTypes.MESSAGE;


public enum Messages {
  ;
  @XmlAccessorType(XmlAccessType.FIELD)
  public static final class Message extends CommandsTypes.Command implements DTOInterfaces.MESSAGE {
    @XmlElement(name = "message")
    private String message;

    public Message(String message) {
      super(MESSAGE.getType());
      this.message = message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public boolean equals(final Object o) {
      if (o == this) return true;
      if (!(o instanceof Messages.Message other)) return false;
      if (!other.canEqual(this)) return false;
      final Object this$message = this.getMessage();
      final Object other$message = other.getMessage();
      return Objects.equals(this$message, other$message);
    }

    protected boolean canEqual(final Object other) {
      return other instanceof Messages.Message;
    }

    public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      final Object $message = this.getMessage();
      result = result * PRIME + ($message == null ? 43 : $message.hashCode());
      return result;
    }

    public String toString() {
      return "RequestDTO.Commands.Message(message=" + this.getMessage() + ")";
    }

    @Override
    public String getMessage() {
      return message;
    }
  }
  
}
