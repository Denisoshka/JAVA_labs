package javachat.client.model.chat_modules.interfaces;

import javachat.client.model.dto.RequestDTO;

import java.util.List;

public interface ChatModule {
  void commandAction(RequestDTO.BaseCommand command, List<Object> args);

  void responseActon(RequestDTO.BaseCommand command);

  void eventAction(RequestDTO.BaseEvent event);
}
