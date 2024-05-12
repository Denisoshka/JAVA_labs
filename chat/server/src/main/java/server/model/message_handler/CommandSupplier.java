package server.model.message_handler;

public interface CommandSupplier {
  CommandInterface getCommand(String command);
}
