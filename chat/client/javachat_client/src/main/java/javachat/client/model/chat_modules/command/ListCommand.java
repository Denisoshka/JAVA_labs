package javachat.client.model.chat_modules.command;

import javachat.client.model.dto.RequestDTO;
import javachat.client.model.dto.subtypes.ListDTO;
import javachat.client.model.main_context.ChatSessionExecutor;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

public class ListCommand implements ChatCommand {
  private final ChatSessionExecutor chatSessionExecutor;
  private final Logger modulelogger;
  private final Logger defaultLoger;

  public ListCommand(ChatSessionExecutor chatSessionExecutor) {
    this.chatSessionExecutor = chatSessionExecutor;
    this.modulelogger = chatSessionExecutor.getModuleLogger();
    this.defaultLoger = chatSessionExecutor.getDefaultLogger();
  }

  @Override
  public void commandAction(RequestDTO.BaseCommand command, List<Object> args) throws InterruptedException {
    final var ioProcessor = chatSessionExecutor.getIOProcessor();
    final var converter = (ListDTO.ListDTOConverter) chatSessionExecutor.getXMLDTOConverterManager().getConverter(RequestDTO.DTO_SECTION.LIST);
    chatSessionExecutor.executeAction(() -> {
              try {
                chatSessionExecutor.executeResponse(this::responseActon);
                ioProcessor.sendMessage(converter.serialize(new ListDTO.Command()));
              } catch (IOException e) {
                chatSessionExecutor.getModuleLogger().info(e.getMessage(), e);
              }
            }
    );
  }

  @Override
  public void responseActon() {
    try {
      final var response = (RequestDTO.BaseResponse) chatSessionExecutor.getModuleExchanger().take();
      final var responseType = response.getResponseType();
      if (responseType == RequestDTO.BaseResponse.RESPONSE_TYPE.ERROR) {
        modulelogger.info(((ListDTO.Error) response).getMessage());
      } else if (responseType == RequestDTO.BaseResponse.RESPONSE_TYPE.SUCCESS) {
        ListDTO.Success success = (ListDTO.Success) response;
        chatSessionExecutor.getController().showUsers(success.getUsers());
      }
    } catch (InterruptedException e) {
      //todo
    }
  }

  @Override
  public void eventAction(RequestDTO.BaseEvent event) {
//    unhandled list event
  }
}
