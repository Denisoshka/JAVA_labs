package javachat.client.model.DTO.TestClass;

import java.util.Objects;

public class TestClass {
//  @XmlRootElement(name = "event")
//  @XmlAccessorType(XmlAccessType.FIELD)
  public class Event {
//    @XmlAttribute
    private String name;

    /*@XmlElement
    @XmlElements({
            @XmlElement(name="name", type=LoginEvent.class),
            @XmlElement(name="text", type=MessageEvent.class),
            @XmlElement(name="name", type=LogoutEvent.class)
    })*/
    private EventData data;

    // Getters and setters
  }

  public class EventData {
    // Base class for event data
  }

  public class LoginEvent extends EventData {
    // Login event data specific fields
  }

  public class MessageEvent extends EventData {
    // Message event data specific fields
  }

  public class LogoutEvent extends EventData {
    // Logout event data specific fields
  }

}
