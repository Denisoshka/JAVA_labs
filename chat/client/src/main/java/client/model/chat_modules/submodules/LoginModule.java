package client.model.chat_modules.submodules;

import client.facade.ChatSessionController;
import client.model.chat_modules.interfaces.ChatModule;
import client.model.main_context.ChatSessionExecutor;
import dto.DataDTO;
import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.interfaces.DTOInterfaces;
import dto.subtypes.LoginDTO;
import org.slf4j.Logger;

import java.io.IOException;

public class LoginModule implements ChatModule<DataDTO.LoginData> {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(LoginModule.class);
  private final ChatSessionExecutor chatSessionExecutor;
  private final LoginDTO.LoginDTOConverter converter;
  private final ChatSessionController chatSessionController;

  private DataDTO.LoginData loginData = null;

  public LoginModule(ChatSessionExecutor chatSessionExecutor) {
    this.chatSessionExecutor = chatSessionExecutor;
    this.chatSessionController = chatSessionExecutor.getChatSessionController();
    this.converter = (LoginDTO.LoginDTOConverter) chatSessionExecutor.getDTOConverterManager().getConverterBySection(RequestDTO.DTO_SECTION.LOGIN);
  }

  @Override
  public void commandAction(DTOInterfaces.COMMAND_DTO command, DataDTO.LoginData additionalArg) {
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
        ioProcessor.sendMessage(converter.serialize(new LoginDTO.Command(additionalArg.getName(), additionalArg.getPassword())).getBytes());
        responseActon(null);
      } catch (UnableToSerialize e) {
        log.info(e.getMessage(), e);
      } catch (IOException e) {
        try {
          chatSessionExecutor.shutdownConnection();
        } catch (IOException _) {
        }
        log.info(e.getMessage(), e);
      }
    });
  }

  @Override
  public void responseActon(DTOInterfaces.COMMAND_DTO command) {
    chatSessionExecutor.executeModuleAction(() -> {
      String nodeName = null;
      try {
        final var tree = chatSessionExecutor.getModuleExchanger().take();
        final DTOInterfaces.RESPONSE_DTO response = (DTOInterfaces.RESPONSE_DTO) converter.deserialize(tree);
        RequestDTO.RESPONSE_TYPE status = response.getResponseType();
        if (status == RequestDTO.RESPONSE_TYPE.SUCCESS) {
          log.info("login successful");
          chatSessionController.onLoginCommand(status);
        } else if (status == RequestDTO.RESPONSE_TYPE.ERROR) {
          log.info(((LoginDTO.Error) response).getMessage());
        }
      } catch (UnableToDeserialize e) {
        log.error(e.getMessage(), e);
      } catch (InterruptedException _) {
      }
    });
  }

  @Override
  public void eventAction(DTOInterfaces.EVENT_DTO event) {
    chatSessionController.onLoginEvent((LoginDTO.Event) event);
  }
}

