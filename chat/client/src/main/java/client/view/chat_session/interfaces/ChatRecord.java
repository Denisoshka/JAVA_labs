package client.view.chat_session.interfaces;

import client.view.chat_session.ChatSession;

public interface ChatRecord {
  ChatSession.ChatEventType getType();
}
