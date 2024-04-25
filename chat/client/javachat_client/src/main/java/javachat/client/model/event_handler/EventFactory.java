package javachat.client.model.event_handler;

import java.util.HashMap;
import java.util.Map;

public class EventFactory implements EventSupplier {
  private Map<String, EventInterface> events;

  public EventFactory() {
    this.events = new HashMap<>();
//    this.events.put("list", new ListCommand());
//    this.events.put("message", new MessageCommand());
//    this.events.put("logout", new LogoutCommand());
  }

  @Override
  public EventInterface getCommand(String command) {
    return events.get(command);
  }
}
