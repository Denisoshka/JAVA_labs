package client.model.chat_modules.submodules;

import client.facade.ChatSessionController;
import client.model.chat_modules.interfaces.ChatModule;
import client.model.main_context.ChatSessionExecutor;
import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.interfaces.DTOInterfaces;
import dto.subtypes.MessageDTO;
import org.slf4j.Logger;

import java.io.IOException;

public class MessageModule implements ChatModule<Object> {
  private final ChatSessionExecutor chatSessionExecutor;
  private final ChatSessionController chatSessionController;
  private final MessageDTO.MessageDTOConverter converter;
  private final Logger modulelogger;
  private final Logger defaultLoger;

  public MessageModule(ChatSessionExecutor chatSessionExecutor) {
    this.chatSessionExecutor = chatSessionExecutor;
    this.chatSessionController = chatSessionExecutor.getChatSessionController();
    this.modulelogger = chatSessionExecutor.getModuleLogger();
    this.defaultLoger = chatSessionExecutor.getDefaultLogger();
    this.converter = (MessageDTO.MessageDTOConverter) chatSessionExecutor.getDTOConverterManager().getConverterBySection(RequestDTO.DTO_SECTION.MESSAGE);
  }

  @Override
  public void commandAction(DTOInterfaces.COMMAND_DTO command, Object additionalArg) {
    modulelogger.info(STR."sending msg: \{((MessageDTO.Command) command).getMessage()}");
    var ioProcessor = chatSessionExecutor.getIOProcessor();
    var converter = chatSessionExecutor.getDTOConverterManager();
    chatSessionExecutor.executeModuleAction(() -> {
      try {
        responseActon(command);
        ioProcessor.sendMessage(converter.serialize(command).getBytes());
        modulelogger.info(STR."send msg: \{((MessageDTO.Command) command).getMessage()}");
      } catch (IOException e) {
        modulelogger.warn(e.getMessage(), e);
      }
    });
  }

  @Override
  public void responseActon(DTOInterfaces.COMMAND_DTO command) {
    chatSessionExecutor.executeModuleAction(() -> {
      try {
        final var response = (DTOInterfaces.RESPONSE_DTO) converter.deserialize(chatSessionExecutor.getModuleExchanger().take());
        RequestDTO.RESPONSE_TYPE status = response.getResponseType();
        if (status != RequestDTO.RESPONSE_TYPE.SUCCESS) {
          modulelogger.info(((DTOInterfaces.ERROR_RESPONSE_DTO) response).getMessage());
        }
      } catch (InterruptedException _) {
      } catch (UnableToDeserialize e) {
        modulelogger.warn(e.getMessage(), e);
      }
    });
  }

  @Override
  public void eventAction(DTOInterfaces.EVENT_DTO event) {
    chatSessionController.onMessageEvent((MessageDTO.Event) event);
  }
}
