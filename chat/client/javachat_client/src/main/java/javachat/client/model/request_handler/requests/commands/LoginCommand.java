package javachat.client.model.request_handler.requests.commands;

import javachat.client.model.ChatSessionExecutor;
import javachat.client.model.Connection;
import javachat.client.model.DTO.commands.COMMAND_SECTION;
import javachat.client.model.request_handler.IOHandler;

public class LoginCommand implements CommandRequest {
  @Override
  public void performCommand(Connection connection, ChatSessionExecutor chatSession,
                             IOHandler handler, COMMAND_SECTION.Command command) {
    javachat.client.model.DTO.commands.LoginCommand loginCommand = (javachat.client.model.DTO.commands.LoginCommand) command;

  }
}
