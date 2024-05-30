package client.model.chat_modules.submodules;

import client.facade.ChatSessionController;
import client.model.chat_modules.interfaces.ChatModule;
import client.model.main_context.ChatSessionExecutor;
import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.interfaces.DTOInterfaces;
import dto.subtypes.login.LoginCommand;
import dto.subtypes.login.LoginDTOConverter;
import dto.subtypes.login.LoginError;
import dto.subtypes.login.LoginEvent;
import dto.subtypes.other.LoginData;
import org.slf4j.Logger;
import org.w3c.dom.Document;

import java.io.IOException;

public class LoginModule implements ChatModule<LoginData> {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(LoginModule.class);
  private final ChatSessionExecutor chatSessionExecutor;
  private final LoginDTOConverter converter;
  private final ChatSessionController chatSessionController;

  private LoginData loginData = null;


  public LoginModule(ChatSessionExecutor chatSessionExecutor) {
    this.chatSessionExecutor = chatSessionExecutor;
    this.chatSessionController = chatSessionExecutor.getChatSessionController();
    this.converter = (LoginDTOConverter) chatSessionExecutor.getDTOConverterManager().getConverterBySection(RequestDTO.DTO_SECTION.LOGIN);
  }


  public void commandAction(DTOInterfaces.COMMAND_DTO command, LoginData additionalArg) {
    String hostname = additionalArg.getHostname();
    int port = additionalArg.getPort();

    chatSessionExecutor.executeModuleAction(() -> {
      try {
        if (!chatSessionExecutor.isConnected()) {
          loginData = additionalArg;
        }
        if (!chatSessionExecutor.isConnected()) {
          chatSessionExecutor.introduceConnection(hostname, port);
        }
        var ioProcessor = chatSessionExecutor.getIOProcessor();
        ioProcessor.sendMessage(converter.serialize(new LoginCommand(additionalArg.getName(), additionalArg.getPassword())).getBytes());
        responseActon(null);
      } catch (UnableToSerialize e) {
        log.error(e.getMessage(), e);
      } catch (IOException e) {
        try {
          chatSessionExecutor.shutdownConnection();
        } catch (IOException _) {
        }
        log.error(e.getMessage(), e);
      }
    });
  }


  public void responseActon(DTOInterfaces.COMMAND_DTO command) {
    chatSessionExecutor.executeModuleAction(() -> {
      try {
        final var tree = chatSessionExecutor.getModuleExchanger().take();
        final DTOInterfaces.RESPONSE_DTO response = (DTOInterfaces.RESPONSE_DTO) converter.deserialize(tree);
        RequestDTO.RESPONSE_TYPE status = response.getResponseType();
        if (status == RequestDTO.RESPONSE_TYPE.SUCCESS) {
          log.info("login successful");
          chatSessionController.onLoginCommand(status);
        } else if (status == RequestDTO.RESPONSE_TYPE.ERROR) {
          log.info(((LoginError) response).getMessage());
        }
      } catch (UnableToDeserialize e) {
        log.error(e.getMessage(), e);
      } catch (InterruptedException _) {
      }
    });
  }


  public void eventAction(Document root) {
    try {
      chatSessionController.onLoginEvent((LoginEvent) converter.deserialize(root));
    } catch (UnableToDeserialize e) {
      log.error(e.getMessage(), e);
    }
  }
}

