package javachat.client.model.chat_modules.command;

import javachat.client.model.dto.RequestDTO;

import java.util.List;

public interface ChatCommand {
  void commandAction(RequestDTO.BaseCommand command, List<Object> args) throws InterruptedException;

  void responseActon();

  void eventAction(RequestDTO.BaseEvent event);
}
