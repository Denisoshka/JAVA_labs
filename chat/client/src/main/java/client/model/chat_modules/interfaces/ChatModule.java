package client.model.chat_modules.interfaces;

import dto.interfaces.DTOInterfaces;

public interface ChatModule<T> {
  void eventAction(DTOInterfaces.EVENT_DTO event);
}
