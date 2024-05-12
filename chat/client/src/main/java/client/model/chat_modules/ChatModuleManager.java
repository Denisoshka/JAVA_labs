package client.model.chat_modules;

import client.model.chat_modules.interfaces.AbstractChatModuleManager;
import client.model.chat_modules.interfaces.ChatModule;
import client.model.chat_modules.submodules.ListModule;
import client.model.chat_modules.submodules.LoginModule;
import client.model.chat_modules.submodules.LogoutModule;
import client.model.chat_modules.submodules.MessageModule;
import dto.RequestDTO;
import client.model.main_context.ChatSessionExecutor;

import java.util.HashMap;
import java.util.Properties;

public class ChatModuleManager implements AbstractChatModuleManager {
  private final HashMap<RequestDTO.DTO_SECTION, ChatModule> modules;

  public ChatModuleManager(Properties properties, ChatSessionExecutor chatSessionExecutor) {
    modules = new HashMap<>(4);
    modules.put(RequestDTO.DTO_SECTION.LIST, new ListModule(chatSessionExecutor));
    modules.put(RequestDTO.DTO_SECTION.MESSAGE, new MessageModule(chatSessionExecutor));
    modules.put(RequestDTO.DTO_SECTION.LOGIN, new LoginModule(chatSessionExecutor));
    modules.put(RequestDTO.DTO_SECTION.LOGOUT, new LogoutModule(chatSessionExecutor));
  }

  @Override
  public ChatModule getChatModule(RequestDTO.DTO_SECTION moduleSection) {
    return modules.get(moduleSection);
  }
}
