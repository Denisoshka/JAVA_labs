package javachat.client.model.chatModules.command;

import javachat.client.facade.ChatSessionController;
import javachat.client.model.DTO.RequestDTO;
import javachat.client.model.DTO.XyiDTO;
import javachat.client.model.DTO.exceptions.UnableToSerialize;
import javachat.client.model.DTO.subtypes.LoginDTO;
import javachat.client.model.main_context.ChatSessionExecutor;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

public class LoginCommand implements ChatCommand {
  private final ChatSessionExecutor chatSessionExecutor;
  private final ChatSessionController controller;
  private final Logger modulelogger;
  private final Logger defaultLoger;

  private XyiDTO.LoginData loginData;

  LoginCommand(ChatSessionExecutor chatSessionExecutor) {
    this.chatSessionExecutor = chatSessionExecutor;
    this.controller = chatSessionExecutor.getController();
    this.modulelogger = chatSessionExecutor.getModuleLogger();
    this.defaultLoger = chatSessionExecutor.getDefaultLogger();
  }

  @Override
  public void commandAction(RequestDTO.BaseCommand command, List<Object> args) throws InterruptedException {
    XyiDTO.LoginData data = (XyiDTO.LoginData) args.getFirst();
    String hostname = data.getHostname();
    int port = data.getPort();
    chatSessionExecutor.executeAction(() -> {
      try {
        if (loginData == null || !loginData.getHostname().equals(hostname) &&
                loginData.getPort() != port) {
          chatSessionExecutor.introduceConnection(hostname, port);
          loginData = data;
        }
        var ioProcessor = chatSessionExecutor.getIOProcessor();
        var converter = chatSessionExecutor.getXMLDTOConverterManager();

        chatSessionExecutor.executeResponse(this::responseActon);
        ioProcessor.sendMessage(converter.serialize(new LoginDTO.Command(data.getName(), data.getPassword())));

      } catch (UnableToSerialize e) {
        modulelogger.info(e.getMessage(), e);
      } catch (IOException e) {
        defaultLoger.info(e.getMessage(), e);
      }
    });

  }

  @Override
  public void responseActon() {
    try {
      final var response = (RequestDTO.BaseResponse) chatSessionExecutor.getModuleExchanger().take();
      RequestDTO.BaseResponse.RESPONSE_TYPE status = response.getResponseType();
      if (status == RequestDTO.BaseResponse.RESPONSE_TYPE.SUCCESS) {
        onLoginSuccess();
      } else if (status == RequestDTO.BaseResponse.RESPONSE_TYPE.ERROR) {
        modulelogger.info(((LoginDTO.Error) response).getMessage());
      }


    } catch (InterruptedException e) {

    }

  }

  private void onLoginSuccess() {

  }

  @Override
  public void eventAction(RequestDTO.BaseEvent event) {

  }
}

