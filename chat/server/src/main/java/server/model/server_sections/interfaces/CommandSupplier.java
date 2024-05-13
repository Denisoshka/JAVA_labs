package server.model.server_sections;

public interface CommandSupplier {
  AbstractSection getCommand(String command);
}
