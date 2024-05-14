package client.model.chat_modules.submodules;

import client.facade.ChatSessionController;
import client.model.chat_modules.interfaces.ChatModule;
import client.model.main_context.ChatSessionExecutor;
import dto.RequestDTO;
import dto.subtypes.LogoutDTO;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

public class LogoutModule implements ChatModule {

  private final ChatSessionExecutor chatSessionExecutor;

  private final ChatSessionController chatSessionController;
  private final Logger modulelogger;
  private final Logger defaultLoger;

  public LogoutModule(ChatSessionExecutor chatSessionExecutor) {
    this.chatSessionExecutor = chatSessionExecutor;
    this.chatSessionController = chatSessionExecutor.getChatSessionController();
    this.modulelogger = chatSessionExecutor.getModuleLogger();
    this.defaultLoger = chatSessionExecutor.getDefaultLogger();
  }

  @Override
  public void commandAction(RequestDTO.BaseCommand command, List<Object> args) {
    var ioProcessor = chatSessionExecutor.getIOProcessor();
    var converter = chatSessionExecutor.getDTOConverterManager();
    chatSessionExecutor.executeAction(() -> {
      try {
        responseActon(null);
        ioProcessor.sendMessage(converter.serialize(new LogoutDTO.Command()).getBytes());
      } catch (IOException e) {
        modulelogger.warn(e.getMessage());
      }
    });
  }

  @Override
  public void responseActon(RequestDTO.BaseCommand command) {
//    todo
  }

  @Override
  public void eventAction(RequestDTO.BaseEvent event) {
    chatSessionController.onLogoutEvent((LogoutDTO.Event) event);
  }
}
