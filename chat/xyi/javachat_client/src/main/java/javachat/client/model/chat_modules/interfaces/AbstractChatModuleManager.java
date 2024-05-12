package javachat.client.model.chat_modules.interfaces;

import javachat.client.model.dto.RequestDTO;

public interface AbstractChatModuleManager {
  ChatModule getChatModule(RequestDTO.DTO_SECTION moduleSection);
}
