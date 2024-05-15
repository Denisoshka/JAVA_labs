package client.model.chat_modules.submodules;

import client.facade.ChatSessionController;
import client.model.chat_modules.interfaces.ChatModule;
import client.model.main_context.ChatSessionExecutor;
import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.subtypes.LogoutDTO;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

public class LogoutModule implements ChatModule {

  private final ChatSessionExecutor chatSessionExecutor;

  private final ChatSessionController chatSessionController;
  private final Logger modulelogger;
  private final Logger defaultLoger;
  private final LogoutDTO.LogoutDTOConverter converter;

  public LogoutModule(ChatSessionExecutor chatSessionExecutor) {
    this.chatSessionExecutor = chatSessionExecutor;
    this.chatSessionController = chatSessionExecutor.getChatSessionController();
    this.modulelogger = chatSessionExecutor.getModuleLogger();
    this.defaultLoger = chatSessionExecutor.getDefaultLogger();
    this.converter = (LogoutDTO.LogoutDTOConverter) chatSessionExecutor.getDTOConverterManager().getConverter(RequestDTO.DTO_SECTION.LOGOUT);
  }

  @Override
  public void commandAction(RequestDTO.BaseCommand command, List<Object> args) {
    var ioProcessor = chatSessionExecutor.getIOProcessor();
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
    chatSessionExecutor.executeAction(() -> {
      try {
        final var response = (RequestDTO.BaseResponse) converter.deserialize(chatSessionExecutor.getModuleExchanger().take());
        if (response.getResponseType() == RequestDTO.BaseResponse.RESPONSE_TYPE.ERROR) {
          modulelogger.info(((RequestDTO.BaseErrorResponse) response).getMessage());
        }
        chatSessionExecutor.shutdownConnection();
      } catch (IOException e) {
        modulelogger.warn(e.getMessage(), e);
      } catch (InterruptedException _) {
      }
    });
//    todo
  }

  @Override
  public void eventAction(RequestDTO.BaseEvent event) {
    chatSessionController.onLogoutEvent((LogoutDTO.Event) event);
  }
}
