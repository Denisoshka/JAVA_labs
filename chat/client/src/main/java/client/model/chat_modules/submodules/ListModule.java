package client.model.chat_modules.submodules;

import client.facade.ChatSessionController;
import client.model.chat_modules.interfaces.ChatModule;
import client.model.main_context.ChatSessionExecutor;
import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.subtypes.ListDTO;
import org.slf4j.Logger;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;

public class ListModule implements ChatModule {
  private final ChatSessionExecutor chatSessionExecutor;
  private final ChatSessionController chatSessionController;
  private final ListDTO.ListDTOConverter converter;
  private final Logger modulelogger;
  private final Logger defaultLoger;

  public ListModule(ChatSessionExecutor chatSessionExecutor) {
    this.chatSessionExecutor = chatSessionExecutor;
    this.converter = (ListDTO.ListDTOConverter) chatSessionExecutor.getDTOConverterManager().getConverter(RequestDTO.DTO_SECTION.LIST);
    this.chatSessionController = chatSessionExecutor.getChatSessionController();
    this.modulelogger = chatSessionExecutor.getModuleLogger();
    this.defaultLoger = chatSessionExecutor.getDefaultLogger();
  }

  @Override
  public void commandAction(RequestDTO.BaseCommand command, List<Object> args) {
    final var ioProcessor = chatSessionExecutor.getIOProcessor();
//    final var converter = (ListDTO.ListDTOConverter) chatSessionExecutor.getDTOConverterManager().getConverter(RequestDTO.DTO_SECTION.LIST);
    chatSessionExecutor.executeAction(() -> {
              try {
                responseActon(null);
                ioProcessor.sendMessage(converter.serialize(new ListDTO.Command()).getBytes());
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
        var docResponse = chatSessionExecutor.getModuleExchanger().take();
        final var response = (RequestDTO.BaseResponse) converter.deserialize(docResponse);

        try {
          javax.xml.transform.TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory.newInstance();
          // Создаем Transformer
          javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();

          // Настраиваем преобразователь для удобного чтения
//          transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
//          transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

          // Создаем строковый Writer для сохранения результата
          java.io.StringWriter writer = new java.io.StringWriter();

          // Преобразуем Document в StreamResult
          javax.xml.transform.dom.DOMSource domSource = new javax.xml.transform.dom.DOMSource(docResponse);
          javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(writer);

          // Преобразуем DOM в строку
          transformer.transform(domSource, result);

          // Возвращаем строковое представление XML
          modulelogger.debug(writer.toString());
        } catch (TransformerException _) {
        }

        modulelogger.info(response.toString());
        chatSessionController.onListResponse(null, response);
      } catch (InterruptedException _) {
      } catch (UnableToDeserialize e) {
        modulelogger.warn(e.getMessage(), e);
      }
    });
  }

  @Override
  public void eventAction(RequestDTO.BaseEvent event) {
    modulelogger.warn("unimplemented event");
//    unhandle list event
  }
}
