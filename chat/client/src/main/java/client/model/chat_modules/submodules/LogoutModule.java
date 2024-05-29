package client.model.chat_modules.submodules;

import client.facade.ChatSessionController;
import client.model.chat_modules.interfaces.ChatModule;
import client.model.main_context.ChatSessionExecutor;
import client.model.main_context.interfaces.ConnectionModule;
import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.interfaces.DTOInterfaces;
import org.slf4j.Logger;
import org.w3c.dom.Document;

import java.io.IOException;

public class LogoutModule implements ChatModule<Object> {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(LogoutModule.class);

  private final ChatSessionExecutor chatSessionExecutor;
  private final ChatSessionController chatSessionController;
  private final LogoutDTO.LogoutDTOConverter converter;

  public LogoutModule(ChatSessionExecutor chatSessionExecutor) {
    this.chatSessionExecutor = chatSessionExecutor;
    this.chatSessionController = chatSessionExecutor.getChatSessionController();
    this.converter = (LogoutDTO.LogoutDTOConverter) chatSessionExecutor.getDTOConverterManager().getConverterBySection(RequestDTO.DTO_SECTION.LOGOUT);
  }

  public void commandAction(DTOInterfaces.COMMAND_DTO command, Object additionalArg) {
    var ioProcessor = chatSessionExecutor.getIOProcessor();
    chatSessionExecutor.executeModuleAction(() -> {
      try {
        responseActon(null);
        ioProcessor.sendMessage(converter.serialize(new LogoutDTO.Command()).getBytes());
      } catch (IOException e) {
        log.warn(e.getMessage());
      }
    });
  }

  public void responseActon(DTOInterfaces.COMMAND_DTO command) {
    chatSessionExecutor.executeModuleAction(() -> {
      try {
        final var response = (DTOInterfaces.RESPONSE_DTO) converter.deserialize(chatSessionExecutor.getModuleExchanger().take());
        if (response.getResponseType() == RequestDTO.RESPONSE_TYPE.SUCCESS) {
          chatSessionController.onConnectResponse(ConnectionModule.ConnectionState.CONNECTED);
        } else {
          log.info(((DTOInterfaces.ERROR_RESPONSE_DTO) response).getMessage());
        }
        chatSessionController.onConnectResponse(ConnectionModule.ConnectionState.DISCONNECTED);
        chatSessionController.onLogoutCommand(response);
        chatSessionExecutor.shutdownConnection();
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
      chatSessionController.onLogoutEvent((LogoutDTO.Event) event);
    } catch (UnableToDeserialize e) {
      log.warn(e.getMessage(), e);
    }
  }
}
