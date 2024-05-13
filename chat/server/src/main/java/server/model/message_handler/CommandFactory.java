package server.model.message_handler;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory implements CommandSupplier {
  private Map<String, CommandInterface> commands;

  public CommandFactory() {
    this.commands = new HashMap<>();
    this.commands.put("list", new ListCommand());
    this.commands.put("message", new MessageCommand());
    this.commands.put("logout", new LogoutSection());
  }

  @Override
  public CommandInterface getCommand(String command) {
    return commands.get(command);
  }
}
