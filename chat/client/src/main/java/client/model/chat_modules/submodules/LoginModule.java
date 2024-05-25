package client.model.chat_modules.submodules;

import client.facade.ChatSessionController;
import client.model.chat_modules.interfaces.ChatModule;
import client.model.main_context.ChatSessionExecutor;
import dto.DataDTO;
import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.subtypes.LoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.io.IOException;

@Slf4j
public class LoginModule implements ChatModule<DataDTO.LoginData> {
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
    this.converter = (LoginDTO.LoginDTOConverter) chatSessionExecutor.getDTOConverterManager().getConverterBySection(RequestDTO.DTO_SECTION.LOGIN);
  }

  @Override
  public void commandAction(RequestDTO.BaseCommand command, DataDTO.LoginData additionalArg) {
    String hostname = additionalArg.getHostname();
    int port = additionalArg.getPort();
    if (loginData != null && loginData.getHostname().equals(hostname) && loginData.getPort() == port && chatSessionExecutor.isConnected()) {
//todo make on logout
      modulelogger.info("login request repeated");
      return;
    }

    chatSessionExecutor.executeModuleAction(() -> {
      try {
        if (!chatSessionExecutor.isConnected()) {
          loginData = additionalArg;
        }
        chatSessionExecutor.introduceConnection(hostname, port);
        var ioProcessor = chatSessionExecutor.getIOProcessor();
        responseActon(null);
        ioProcessor.sendMessage(converter.serialize(new LoginDTO.Command(additionalArg.getName(), additionalArg.getPassword())).getBytes());
      } catch (UnableToSerialize e) {
        log.info(e.getMessage(), e);
      } catch (IOException e) {
        log.info(e.getMessage(), e);
      }
    });
  }

  @Override
  public void responseActon(RequestDTO.BaseCommand command) {
    chatSessionExecutor.executeModuleAction(() -> {
      String nodeName = null;
      try {
        final var tree = chatSessionExecutor.getModuleExchanger().take();
        nodeName = tree.getDocumentElement().getNodeName();
        log.error(tree.getDocumentElement().getNodeName());
        final var response = (RequestDTO.BaseResponse) converter.deserialize(tree);
        RequestDTO.BaseResponse.RESPONSE_TYPE status = response.getResponseType();
        if (status == RequestDTO.BaseResponse.RESPONSE_TYPE.SUCCESS) {
          log.info("login successful");
          chatSessionController.onLoginCommand(status);
        } else if (status == RequestDTO.BaseResponse.RESPONSE_TYPE.ERROR) {
          log.info(((LoginDTO.Error) response).getMessage());
        }
      } catch (UnableToDeserialize e) {
        log.warn(STR."Node name \{nodeName}");
        log.warn(e.getMessage(), e);
      } catch (InterruptedException _) {
      }
    });
  }

  @Override
  public void eventAction(RequestDTO.BaseEvent event) {
    chatSessionController.onLoginEvent((LoginDTO.Event) event);
  }
}

