package client.model.chat_modules.submodules;

import client.facade.ChatSessionController;
import client.model.chat_modules.interfaces.ChatModule;
import client.model.main_context.ChatSessionExecutor;
import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.subtypes.MessageDTO;
import org.slf4j.Logger;

import java.io.IOException;

public class MessageModule implements ChatModule {
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
  public void commandAction(RequestDTO.BaseCommand command, Object additionalArg) {
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
  public void responseActon(RequestDTO.BaseCommand command) {
    chatSessionExecutor.executeModuleAction(() -> {
      try {
        final var response = (RequestDTO.BaseResponse) converter.deserialize(chatSessionExecutor.getModuleExchanger().take());
        RequestDTO.BaseResponse.RESPONSE_TYPE status = response.getResponseType();
        if (status == RequestDTO.BaseResponse.RESPONSE_TYPE.SUCCESS) {
          chatSessionController.onMessageResponse((MessageDTO.Command) command, null);
        } else {
          modulelogger.info(((RequestDTO.BaseErrorResponse) response).getMessage());
        }
      } catch (InterruptedException _) {
      } catch (UnableToDeserialize e) {
        modulelogger.warn(e.getMessage(), e);
      }
    });
  }

  @Override
  public void eventAction(RequestDTO.BaseEvent event) {
    chatSessionController.onMessageEvent((MessageDTO.Event) event);
  }
}
