package client.model.chat_modules.interfaces;

import dto.interfaces.DTOInterfaces;

public interface ChatModule<T> {
  void commandAction(DTOInterfaces.COMMAND_DTO command, T additionalArg);

  void responseActon(DTOInterfaces.COMMAND_DTO command);

  void eventAction(DTOInterfaces.EVENT_DTO event);
}
