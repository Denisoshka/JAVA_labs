package javachat.server.server_model.message_handler;

public interface CommandSupplier {
  CommandInterface getCommand(String command);
}
