package javachat.client.model.event_handler;

public interface EventSupplier {
  EventInterface getCommand(String command);
}
