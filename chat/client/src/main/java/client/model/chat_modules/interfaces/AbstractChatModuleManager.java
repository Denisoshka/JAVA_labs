package client.model.chat_modules.interfaces;

import client.model.dto.RequestDTO;

public interface AbstractChatModuleManager {
  ChatModule getChatModule(RequestDTO.DTO_SECTION moduleSection);
}
