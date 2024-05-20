package client.model.chat_modules.interfaces;

import dto.RequestDTO;

public interface ChatModule {
  void commandAction(RequestDTO.BaseCommand command, Object additionalArg);

  void responseActon(RequestDTO.BaseCommand command);

  void eventAction(RequestDTO.BaseEvent event);
}
