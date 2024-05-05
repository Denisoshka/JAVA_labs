package javachat.client.model.chatModules.command;

import javachat.client.model.DTO.RequestDTO;

import java.util.List;

public interface ChatCommand {
  void commandAction(RequestDTO.BaseCommand command, List<Object> args) throws InterruptedException;

  void responseActon();

  void eventAction(RequestDTO.BaseEvent event);
}
