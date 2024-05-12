package javachat.client.model.chat_modules.command;

import javachat.client.facade.ChatSessionController;
import javachat.client.model.dto.RequestDTO;
import javachat.client.model.dto.subtypes.MessageDTO;
import javachat.client.model.main_context.ChatSessionExecutor;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

public class MessageModule implements ChatModule {
  private final ChatSessionExecutor chatSessionExecutor;

  private final ChatSessionController chatSessionController;
  private final Logger modulelogger;
  private final Logger defaultLoger;

  public MessageModule(ChatSessionExecutor chatSessionExecutor) {
    this.chatSessionExecutor = chatSessionExecutor;
    this.chatSessionController = chatSessionExecutor.getController();
    this.modulelogger = chatSessionExecutor.getModuleLogger();
    this.defaultLoger = chatSessionExecutor.getDefaultLogger();
  }

  @Override
  public void commandAction(RequestDTO.BaseCommand command, List<Object> args) {
    var ioProcessor = chatSessionExecutor.getIOProcessor();
    var converter = chatSessionExecutor.getXMLDTOConverterManager();
    responseActon(command);
    chatSessionExecutor.executeAction(() -> {
      try {
        ioProcessor.sendMessage(converter.serialize(command));
      } catch (IOException e) {
        modulelogger.warn(e.getMessage(), e);
      }
    });
  }

  @Override
  public void responseActon(RequestDTO.BaseCommand command) {
    chatSessionExecutor.executeAction(() -> {
      try {
        final var response = (RequestDTO.BaseResponse) chatSessionExecutor.getModuleExchanger().take();
        RequestDTO.BaseResponse.RESPONSE_TYPE status = response.getResponseType();
        if (status == RequestDTO.BaseResponse.RESPONSE_TYPE.SUCCESS) {
          chatSessionController.onMessageResponse((MessageDTO.Command) command, null);
        } else {
          modulelogger.info(((RequestDTO.BaseErrorResponse) response).getMessage());
        }
      } catch (InterruptedException _) {
      }
    });

  }

  @Override
  public void eventAction(RequestDTO.BaseEvent event) {

  }
}
