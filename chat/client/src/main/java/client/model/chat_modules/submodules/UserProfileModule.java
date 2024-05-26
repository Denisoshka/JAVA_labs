package client.model.chat_modules.submodules;

import client.facade.ChatSessionController;
import client.model.main_context.ChatSessionExecutor;
import client.model.main_context.interfaces.ConnectionModule;
import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.interfaces.DTOInterfaces;
import dto.subtypes.UserProfileDTO;
import io_processing.IOProcessor;
import org.slf4j.Logger;
import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class UserProfileModule {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserProfileModule.class);
  private final ChatSessionExecutor chatSessionExecutor;
  private final UserProfileDTO.UserProfileDTOConverter converter;
  private final ChatSessionController chatSessionController;
//  private final UserProfileDTO.UpdateAvatarCommandConverter updateAvatarCommandConverter;
//  private final UserProfileDTO.DeleteAvatarCommandConverter deleteAvatarCommandConverter;


  public UserProfileModule(ChatSessionExecutor chatSessionExecutor) {
    this.chatSessionExecutor = chatSessionExecutor;
    this.chatSessionController = chatSessionExecutor.getChatSessionController();
    this.converter = (UserProfileDTO.UserProfileDTOConverter) chatSessionExecutor.getDTOConverterManager().getConverterBySection(RequestDTO.DTO_SECTION.USERPROFILE);
//    this.updateAvatarCommandConverter = converter.getUpdateAvatarCommandConverter();
//    this.deleteAvatarCommandConverter = converter.getDeleteAvatarCommandConverter();
  }

  public void updateAvatarAction(File selectedFile) {
    chatSessionExecutor.executeModuleAction(() -> {
      try {
        var conv = converter.getUpdateAvatarCommandConverter();
        byte[] bytes = Files.readAllBytes(selectedFile.toPath());
        String mimeType = Files.probeContentType(selectedFile.toPath());
        IOProcessor ioProcessor = chatSessionExecutor.getIOProcessor();
        ioProcessor.sendMessage(conv.serialize(new UserProfileDTO.UpdateAvatarCommand(mimeType, bytes.length, bytes)).getBytes());
      } catch (UnableToSerialize e) {
        log.info(e.getMessage(), e);
      } catch (IOException e) {
        try {
          chatSessionExecutor.shutdownConnection();
        } catch (IOException ex) {
        }
//        todo remove from here
        chatSessionController.onConnectResponse(ConnectionModule.ConnectionState.DISCONNECTED);
        log.info(e.getMessage(), e);
      }
    });
  }

  private void updateAvatarResponse(File selectedFile) {
    chatSessionExecutor.executeModuleAction(() -> {
      try {
        Document root = chatSessionExecutor.getModuleExchanger().take();
        var upconf = converter.getUpdateAvatarCommandConverter();
        DTOInterfaces.RESPONSE_DTO response = (DTOInterfaces.RESPONSE_DTO) upconf.deserialize(root);
        chatSessionController.onUpdateAvatar(selectedFile, response);
      } /*catch (UnableToSerialize e) {

      }*/ catch (UnableToDeserialize e) {
      } catch (InterruptedException _) {
      }
    });
  }
}
