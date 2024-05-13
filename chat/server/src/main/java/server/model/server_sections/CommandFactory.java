package server.model.message_handler;

import server.model.Server;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory implements CommandSupplier {
  private Map<String, AbstractSection> commands;

  public CommandFactory(Server server) {
    this.commands = new HashMap<>();
    this.commands.put("list", new ListSection());
    this.commands.put("message", new MessageSection());
    this.commands.put("logout", new LogoutSection());
  }

  @Override
  public AbstractSection getCommand(String command) {
    return commands.get(command);
  }
}
