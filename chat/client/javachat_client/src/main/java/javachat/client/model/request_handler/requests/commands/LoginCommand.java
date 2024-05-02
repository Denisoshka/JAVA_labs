package javachat.client.model.request_handler.requests.commands;

import javachat.client.model.ChatSessionExecutor;
import javachat.client.model.Connection;
import javachat.client.model.DTO.commands.CommandSection;
import javachat.client.model.request_handler.IOHandler;

public class LoginCommand implements CommandRequest {
  @Override
  public void performCommand(Connection connection, ChatSessionExecutor chatSession,
                             IOHandler handler, CommandSection.Command command) {
//    Login loginCommand = (Login) command;

  }
}
