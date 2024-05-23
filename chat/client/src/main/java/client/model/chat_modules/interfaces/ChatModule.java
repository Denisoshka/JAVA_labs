package client.model.chat_modules.interfaces;

import dto.RequestDTO;

public interface ChatModule<T> {
  void commandAction(RequestDTO.BaseCommand command, T additionalArg);

  void responseActon(RequestDTO.BaseCommand command);

  void eventAction(RequestDTO.BaseEvent event);
}
