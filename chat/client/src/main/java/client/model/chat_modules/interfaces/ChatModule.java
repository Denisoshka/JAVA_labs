package client.model.chat_modules.interfaces;

import dto.interfaces.DTOInterfaces;
import org.w3c.dom.Document;

public interface ChatModule<T> {
  void eventAction(Document root);
}
