package client.model.chat_modules;

import client.model.chat_modules.interfaces.AbstractChatModuleManager;
import client.model.chat_modules.interfaces.ChatModule;
import client.model.chat_modules.submodules.*;
import client.model.main_context.ChatSessionExecutor;
import dto.RequestDTO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class ChatModuleManager implements AbstractChatModuleManager {
  private final HashMap<RequestDTO.DTO_SECTION, ChatModule> modules;

  public ChatModuleManager(Properties properties, ChatSessionExecutor chatSessionExecutor) throws IOException {
    modules = new HashMap<>(4);
    modules.put(RequestDTO.DTO_SECTION.LIST, new ListModule(chatSessionExecutor));
    modules.put(RequestDTO.DTO_SECTION.MESSAGE, new MessageModule(chatSessionExecutor));
    modules.put(RequestDTO.DTO_SECTION.LOGIN, new LoginModule(chatSessionExecutor));
    modules.put(RequestDTO.DTO_SECTION.LOGOUT, new LogoutModule(chatSessionExecutor));
    modules.put(RequestDTO.DTO_SECTION.FILE, new FileModule(chatSessionExecutor));
    modules.put(RequestDTO.DTO_SECTION.USERPROFILE, new UserProfileModule(chatSessionExecutor));
  }

  @Override
  public ChatModule getChatModule(RequestDTO.DTO_SECTION moduleSection) {
    return modules.get(moduleSection);
  }
}
