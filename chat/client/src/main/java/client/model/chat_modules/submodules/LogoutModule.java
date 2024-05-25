package client.model.chat_modules.submodules;

import client.facade.ChatSessionController;
import client.model.chat_modules.interfaces.ChatModule;
import client.model.main_context.ChatSessionExecutor;
import client.model.main_context.interfaces.ConnectionModule;
import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import dto.subtypes.LogoutDTO;
import org.slf4j.Logger;

import java.io.IOException;

public class LogoutModule implements ChatModule<Object> {

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
    this.converter = (LogoutDTO.LogoutDTOConverter) chatSessionExecutor.getDTOConverterManager().getConverterBySection(RequestDTO.DTO_SECTION.LOGOUT);
  }

  @Override
  public void commandAction(DTOInterfaces.COMMAND_DTO command, Object additionalArg) {
    var ioProcessor = chatSessionExecutor.getIOProcessor();
    chatSessionExecutor.executeModuleAction(() -> {
      try {
        responseActon(null);
        ioProcessor.sendMessage(converter.serialize(new LogoutDTO.Command()).getBytes());
      } catch (IOException e) {
        modulelogger.warn(e.getMessage());
      }
    });
  }

  @Override
  public void responseActon(DTOInterfaces.COMMAND_DTO command) {
    chatSessionExecutor.executeModuleAction(() -> {
      try {
        final var response = (DTOInterfaces.RESPONSE_DTO) converter.deserialize(chatSessionExecutor.getModuleExchanger().take());
        if (response.getResponseType() == RequestDTO.RESPONSE_TYPE.SUCCESS) {
          chatSessionController.onConnectResponse(ConnectionModule.ConnectionState.CONNECTED);
        } else {
          modulelogger.info(((DTOInterfaces.ERROR_RESPONSE_DTO)response).getMessage());
        }
        chatSessionController.onConnectResponse(ConnectionModule.ConnectionState.DISCONNECTED);
        chatSessionController.onLogoutCommand(response);
        chatSessionExecutor.shutdownConnection();
      } catch (IOException e) {
        modulelogger.warn(e.getMessage(), e);
      } catch (InterruptedException _) {
      }
    });
  }

  @Override
  public void eventAction(DTOInterfaces.EVENT_DTO event) {
    chatSessionController.onLogoutEvent((LogoutDTO.Event) event);
  }
}
