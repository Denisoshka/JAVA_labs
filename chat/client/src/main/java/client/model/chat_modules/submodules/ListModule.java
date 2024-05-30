package client.model.chat_modules.submodules;

import client.facade.ChatSessionController;
import client.model.chat_modules.interfaces.ChatModule;
import client.model.main_context.ChatSessionExecutor;
import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.interfaces.DTOInterfaces;
import dto.subtypes.list.ListCommand;
import dto.subtypes.list.ListDTOConverter;
import org.slf4j.Logger;
import org.w3c.dom.Document;

import java.io.IOException;

public class ListModule implements ChatModule {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(ListModule.class);
  private final ChatSessionExecutor chatSessionExecutor;
  private final ChatSessionController chatSessionController;
  private final ListDTOConverter converter;

  public ListModule(ChatSessionExecutor chatSessionExecutor) {
    this.chatSessionExecutor = chatSessionExecutor;
    this.converter = (ListDTOConverter) chatSessionExecutor.getDTOConverterManager().getConverterBySection(RequestDTO.DTO_SECTION.LIST);
    this.chatSessionController = chatSessionExecutor.getChatSessionController();
  }


  public void commandAction(DTOInterfaces.COMMAND_DTO command, Object additionalArg) {
    final var ioProcessor = chatSessionExecutor.getIOProcessor();
    chatSessionExecutor.executeModuleAction(() -> {
              try {
                ioProcessor.sendMessage(converter.serialize(new ListCommand()).getBytes());
                responseActon();
              } catch (IOException e) {
                log.info(e.getMessage(), e);
              }
            }
    );
  }


  private void responseActon() {
    chatSessionExecutor.executeModuleAction(() -> {
      try {
        var docResponse = chatSessionExecutor.getModuleExchanger().take();
        final var response = (DTOInterfaces.RESPONSE_DTO) converter.deserialize(docResponse);
        chatSessionController.onListResponse(null, response);
      } catch (InterruptedException _) {
      } catch (UnableToDeserialize e) {
        log.warn(e.getMessage(), e);
      }
    });
  }


  @Override
  public void eventAction(Document root) {
//    unhandle list event
  }
}
