package client.model.chat_modules.submodules;

import client.facade.ChatSessionController;
import client.model.chat_modules.interfaces.ChatModule;
import client.model.main_context.ChatSessionExecutor;
import dto.DataDTO;
import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.subtypes.LoginDTO;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

public class LoginModule implements ChatModule {
  private final ChatSessionExecutor chatSessionExecutor;
  private final LoginDTO.LoginDTOConverter converter;
  private final ChatSessionController chatSessionController;
  private final Logger modulelogger;
  private final Logger defaultLogger;

  private DataDTO.LoginData loginData = null;

  public LoginModule(ChatSessionExecutor chatSessionExecutor) {
    this.chatSessionExecutor = chatSessionExecutor;
    this.chatSessionController = chatSessionExecutor.getChatSessionController();
    this.modulelogger = chatSessionExecutor.getModuleLogger();
    this.defaultLogger = chatSessionExecutor.getDefaultLogger();
    this.converter = (LoginDTO.LoginDTOConverter) chatSessionExecutor.getDTOConverterManager().getConverter(RequestDTO.DTO_SECTION.LOGIN);
  }

  @Override
  public void commandAction(RequestDTO.BaseCommand command, List<Object> args) {
    DataDTO.LoginData data = (DataDTO.LoginData) args.getFirst();
    String hostname = data.getHostname();
    int port = data.getPort();
    if (loginData != null && loginData.getHostname().equals(hostname) && loginData.getPort() == port) {
      modulelogger.info("login request repeated");
      return;
    }

    chatSessionExecutor.executeAction(() -> {
      try {
        if (loginData == null) {
          loginData = data;
        }
        chatSessionExecutor.introduceConnection(hostname, port);
        var ioProcessor = chatSessionExecutor.getIOProcessor();

        responseActon(null);
        ioProcessor.sendMessage(converter.serialize(new LoginDTO.Command(data.getName(), data.getPassword())).getBytes());
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
        final var response = (RequestDTO.BaseResponse) converter.deserialize(
                chatSessionExecutor.getModuleExchanger().take()
        );
        RequestDTO.BaseResponse.RESPONSE_TYPE status = response.getResponseType();
        if (status == RequestDTO.BaseResponse.RESPONSE_TYPE.SUCCESS) {
          modulelogger.info("login successful");
          onLoginSuccess();
        } else if (status == RequestDTO.BaseResponse.RESPONSE_TYPE.ERROR) {
          modulelogger.info(((LoginDTO.Error) response).getMessage());
        }
      } catch (UnableToDeserialize e) {
        modulelogger.info(e.getMessage(), e);
      } catch (InterruptedException _) {
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

