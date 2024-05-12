package client.model.chat_modules.submodules;

import client.facade.ChatSessionController;
import client.model.chat_modules.interfaces.ChatModule;
import client.model.dto.DataDTO;
import client.model.dto.RequestDTO;
import client.model.dto.exceptions.UnableToSerialize;
import client.model.dto.subtypes.LoginDTO;
import client.model.main_context.ChatSessionExecutor;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

public class LoginModule implements ChatModule {
  private final ChatSessionExecutor chatSessionExecutor;

  private final ChatSessionController chatSessionController;
  private final Logger modulelogger;
  private final Logger defaultLogger;

  private DataDTO.LoginData loginData;

  public LoginModule(ChatSessionExecutor chatSessionExecutor) {
    this.chatSessionExecutor = chatSessionExecutor;
    this.chatSessionController = chatSessionExecutor.getChatSessionController();
    this.modulelogger = chatSessionExecutor.getModuleLogger();
    this.defaultLogger = chatSessionExecutor.getDefaultLogger();
  }

  @Override
  public void commandAction(RequestDTO.BaseCommand command, List<Object> args) {
    DataDTO.LoginData data = (DataDTO.LoginData) args.getFirst();
    String hostname = data.getHostname();
    int port = data.getPort();
    chatSessionExecutor.executeAction(() -> {
      try {
        if (loginData == null || !loginData.getHostname().equals(hostname) && loginData.getPort() != port) {
          chatSessionExecutor.introduceConnection(hostname, port);
          loginData = data;
        }
        var ioProcessor = chatSessionExecutor.getIOProcessor();
        var converter = chatSessionExecutor.getXMLDTOConverterManager();

        responseActon(null);
        ioProcessor.sendMessage(converter.serialize(new LoginDTO.Command(data.getName(), data.getPassword())));
      } catch (UnableToSerialize e) {
        modulelogger.info(e.getMessage(), e);
      } catch (IOException e) {
        defaultLogger.info(e.getMessage(), e);
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
          onLoginSuccess();
        } else if (status == RequestDTO.BaseResponse.RESPONSE_TYPE.ERROR) {
          modulelogger.info(((LoginDTO.Error) response).getMessage());
        }
      } catch (InterruptedException e) {
        modulelogger.info(e.getMessage(), e);
      }
    });
  }

  private void onLoginSuccess() {
    //    todo implements this section
  }

  @Override
  public void eventAction(RequestDTO.BaseEvent event) {
    chatSessionController.onLoginEvent((LoginDTO.Event) event);
  }
}

