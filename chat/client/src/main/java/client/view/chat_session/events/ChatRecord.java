package client.view.chat_session.events;

import client.view.chat_session.ChatSession;

public interface ChatRecord {
  ChatSession.ChatEventType getType();
}
