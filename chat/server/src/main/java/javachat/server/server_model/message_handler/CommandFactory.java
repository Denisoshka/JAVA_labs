package javachat.server.server_model.message_handler;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory implements CommandSupplier {
  private Map<String, CommandInterface> commands;

  public CommandFactory() {
    this.commands = new HashMap<>();
    this.commands.put("list", new ListCommand());
  }

  @Override
  public CommandInterface getCommand(String command) {
    return commands.get(command);
  }
}
