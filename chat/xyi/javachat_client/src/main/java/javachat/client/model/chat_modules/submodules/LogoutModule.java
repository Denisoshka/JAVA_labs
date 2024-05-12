package javachat.client.model.chat_modules.submodules;

import javachat.client.facade.ChatSessionController;
import javachat.client.model.chat_modules.interfaces.ChatModule;
import javachat.client.model.dto.RequestDTO;
import javachat.client.model.dto.subtypes.LogoutDTO;
import javachat.client.model.main_context.ChatSessionExecutor;
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
    var converter = chatSessionExecutor.getXMLDTOConverterManager();
    chatSessionExecutor.executeAction(() -> {
      try {
        responseActon(null);
        ioProcessor.sendMessage(converter.serialize(new LogoutDTO.Command()));
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
