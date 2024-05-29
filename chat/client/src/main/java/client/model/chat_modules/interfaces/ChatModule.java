package client.model.chat_modules.interfaces;

import org.w3c.dom.Document;

public interface ChatModule<T> {
  void eventAction(Document root);
}
