package javachat.client.model.request_handler.requests.commands;

import javachat.client.model.ChatSessionExecutor;
import javachat.client.model.Connection;
import javachat.client.model.DTO.commands.COMMAND_SECTION;
import javachat.client.model.request_handler.IOHandler;

public interface CommandRequest {
  void performCommand(Connection connection, ChatSessionExecutor chatSession,
                      IOHandler handler, COMMAND_SECTION.Command command);
}
