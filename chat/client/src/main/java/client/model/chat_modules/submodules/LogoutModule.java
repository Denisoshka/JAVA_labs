package client.model.chat_modules.submodules;

import client.facade.ChatSessionController;
import client.model.chat_modules.interfaces.ChatModule;
import client.model.main_context.ChatSessionExecutor;
import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.interfaces.DTOInterfaces;
import dto.subtypes.logout.LogoutCommand;
import dto.subtypes.logout.LogoutDTOConverter;
import dto.subtypes.logout.LogoutEvent;
import org.slf4j.Logger;
import org.w3c.dom.Document;

import java.io.IOException;

public class LogoutModule implements ChatModule{
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(LogoutModule.class);

  private final ChatSessionExecutor chatSessionExecutor;
  private final ChatSessionController chatSessionController;
  private final LogoutDTOConverter converter;

  public LogoutModule(ChatSessionExecutor chatSessionExecutor) {
    this.chatSessionExecutor = chatSessionExecutor;
    this.chatSessionController = chatSessionExecutor.getChatSessionController();
    this.converter = (LogoutDTOConverter) chatSessionExecutor.getDTOConverterManager().getConverterBySection(RequestDTO.DTO_SECTION.LOGOUT);
  }

  public void logoutAction() {
    var ioProcessor = chatSessionExecutor.getIOProcessor();
    chatSessionExecutor.executeModuleAction(() -> {
      try {
        responseActon();
        ioProcessor.sendMessage(converter.serialize(new LogoutCommand()).getBytes());
      } catch (IOException e) {
        log.warn(e.getMessage());
      }
    });
  }

  private void responseActon() {
    chatSessionExecutor.executeModuleAction(() -> {
      try {
        final var response = (DTOInterfaces.RESPONSE_DTO) converter.deserialize(chatSessionExecutor.getModuleExchanger().take());
        if (response.getResponseType() == RequestDTO.RESPONSE_TYPE.SUCCESS) {
          chatSessionController.onLogoutResponse(response);
          chatSessionExecutor.shutdownConnection();
        } else {
          log.info(((DTOInterfaces.ERROR_RESPONSE_DTO) response).getMessage());
        }
      } catch (IOException e) {
        log.warn(e.getMessage(), e);
      } catch (InterruptedException _) {
      }
    });
  }

  @Override
  public void eventAction(Document root) {
    try {
      var event = converter.deserialize(root);
      chatSessionController.onLogoutEvent((LogoutEvent) event);
    } catch (UnableToDeserialize e) {
      log.warn(e.getMessage(), e);
    }
  }
}
