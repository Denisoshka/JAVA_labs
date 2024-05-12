package client.model.chat_modules.submodules;

import client.facade.ChatSessionController;
import client.model.chat_modules.interfaces.ChatModule;
import dto.RequestDTO;
import dto.subtypes.ListDTO;
import client.model.main_context.ChatSessionExecutor;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

public class ListModule implements ChatModule {
  private final ChatSessionExecutor chatSessionExecutor;
  private final ChatSessionController chatSessionController;
  private final Logger modulelogger;
  private final Logger defaultLoger;

  public ListModule(ChatSessionExecutor chatSessionExecutor) {
    this.chatSessionExecutor = chatSessionExecutor;

    this.chatSessionController = chatSessionExecutor.getChatSessionController();
    this.modulelogger = chatSessionExecutor.getModuleLogger();
    this.defaultLoger = chatSessionExecutor.getDefaultLogger();
  }

  @Override
  public void commandAction(RequestDTO.BaseCommand command, List<Object> args) {
    final var ioProcessor = chatSessionExecutor.getIOProcessor();
    final var converter = (ListDTO.ListDTOConverter) chatSessionExecutor.getXMLDTOConverterManager().getConverter(RequestDTO.DTO_SECTION.LIST);
    chatSessionExecutor.executeAction(() -> {
              try {
                responseActon(null);
                ioProcessor.sendMessage(converter.serialize(new ListDTO.Command()));
              } catch (IOException e) {
                modulelogger.info(e.getMessage(), e);
              }
            }
    );
  }

  @Override
  public void responseActon(RequestDTO.BaseCommand command) {
    chatSessionExecutor.executeAction(() -> {
      try {
        final var response = (RequestDTO.BaseResponse) chatSessionExecutor.getModuleExchanger().take();
        chatSessionController.onListResponse(null, response);
      } catch (InterruptedException _) {
      }
    });
  }

  @Override
  public void eventAction(RequestDTO.BaseEvent event) {
//    unhandle list event
  }
}
