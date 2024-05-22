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
    this.converter = (LoginDTO.LoginDTOConverter) chatSessionExecutor.getDTOConverterManager().getConverterBySection(RequestDTO.DTO_SECTION.LOGIN);
  }

  @Override
  public void commandAction(RequestDTO.BaseCommand command, Object additionalArg) {
    DataDTO.LoginData data = (DataDTO.LoginData) additionalArg;
    String hostname = data.getHostname();
    int port = data.getPort();
    if (loginData != null && loginData.getHostname().equals(hostname) && loginData.getPort() == port && chatSessionExecutor.isConnected()) {
//todo make on logout
      modulelogger.info("login request repeated");
      return;
    }

    chatSessionExecutor.executeModuleAction(() -> {
      try {
        if (!chatSessionExecutor.isConnected()) {
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
//      final var event = (LoginDTO.Event) converter.deserialize(tree);
    chatSessionExecutor.executeModuleAction(() -> {
      String nodeName = null;
      try {
        final var tree = chatSessionExecutor.getModuleExchanger().take();
        nodeName = tree.getDocumentElement().getNodeName();
        modulelogger.error(tree.getDocumentElement().getNodeName());
        final var response = (RequestDTO.BaseResponse) converter.deserialize(tree);
        RequestDTO.BaseResponse.RESPONSE_TYPE status = response.getResponseType();
        if (status == RequestDTO.BaseResponse.RESPONSE_TYPE.SUCCESS) {
          modulelogger.info("login successful");
          chatSessionController.onLoginCommand(status);
        } else if (status == RequestDTO.BaseResponse.RESPONSE_TYPE.ERROR) {
          modulelogger.info(((LoginDTO.Error) response).getMessage());
        }
      } catch (UnableToDeserialize e) {
        modulelogger.warn(STR."Node name \{nodeName}");
        modulelogger.warn(e.getMessage(), e);
      } catch (InterruptedException _) {
      }
    });
  }

  @Override
  public void eventAction(RequestDTO.BaseEvent event) {
    chatSessionController.onLoginEvent((LoginDTO.Event) event);
  }
}

