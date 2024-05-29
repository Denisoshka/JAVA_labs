package client.model.chat_modules.submodules;

import client.facade.ChatSessionController;
import client.model.chat_modules.interfaces.ChatModule;
import client.model.main_context.ChatSessionExecutor;
import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.interfaces.DTOInterfaces;
import org.slf4j.Logger;
import org.w3c.dom.Document;

import java.io.IOException;

public class MessageModule implements ChatModule<Object> {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(MessageModule.class);
  private final ChatSessionExecutor chatSessionExecutor;
  private final ChatSessionController chatSessionController;
  private final MessageDTO.MessageDTOConverter converter;

  public MessageModule(ChatSessionExecutor chatSessionExecutor) {
    this.chatSessionExecutor = chatSessionExecutor;
    this.chatSessionController = chatSessionExecutor.getChatSessionController();
    this.converter = (MessageDTO.MessageDTOConverter) chatSessionExecutor.getDTOConverterManager().getConverterBySection(RequestDTO.DTO_SECTION.MESSAGE);
  }


  public void commandAction(DTOInterfaces.COMMAND_DTO command, Object additionalArg) {
    log.info(STR."sending msg: \{((MessageDTO.Command) command).getMessage()}");
    var ioProcessor = chatSessionExecutor.getIOProcessor();
    var converter = chatSessionExecutor.getDTOConverterManager();
    chatSessionExecutor.executeModuleAction(() -> {
      try {
        responseActon(command);
        ioProcessor.sendMessage(converter.serialize(command).getBytes());
        log.info(STR."send msg: \{((MessageDTO.Command) command).getMessage()}");
      } catch (IOException e) {
        log.warn(e.getMessage(), e);
      }
    });
  }


  public void responseActon(DTOInterfaces.COMMAND_DTO command) {
    chatSessionExecutor.executeModuleAction(() -> {
      try {
        final var response = (DTOInterfaces.RESPONSE_DTO) converter.deserialize(chatSessionExecutor.getModuleExchanger().take());
        RequestDTO.RESPONSE_TYPE status = response.getResponseType();
        if (status != RequestDTO.RESPONSE_TYPE.SUCCESS) {
          log.info(((DTOInterfaces.ERROR_RESPONSE_DTO) response).getMessage());
        }
      } catch (InterruptedException _) {
      } catch (UnableToDeserialize e) {
        log.warn(e.getMessage(), e);
      }
    });
  }

  @Override
  public void eventAction(Document root) {
    try {
      var event = converter.deserialize(root);
      chatSessionController.onMessageEvent((MessageDTO.Event) event);
    } catch (UnableToDeserialize e) {
      log.error(e.getMessage(), e);
    }
  }
}
