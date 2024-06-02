package client.model.chat_modules.interfaces;

import dto.RequestDTO;

public interface AbstractChatModuleManager {
  ChatModule getChatModule(RequestDTO.DTO_SECTION moduleSection);
}
