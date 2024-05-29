package client.model.chat_modules.submodules;

import client.facade.ChatSessionController;
import client.model.chat_modules.interfaces.ChatModule;
import client.model.main_context.ChatSessionExecutor;
import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.interfaces.DTOConverterManagerInterface;
import dto.interfaces.DTOInterfaces;
import dto.subtypes.user_profile.DeleteAvatarCommand;
import dto.subtypes.user_profile.UpdateAvatarCommand;
import dto.subtypes.user_profile.UpdateAvatarEvent;
import dto.subtypes.user_profile.UserProfileDTOConverter;
import org.slf4j.Logger;
import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class UserProfileModule implements ChatModule {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserProfileModule.class);
  private final ChatSessionExecutor chatSessionExecutor;
  private final UserProfileDTOConverter converter;
  private final ChatSessionController chatSessionController;
//  private final UserProfileDTO.UpdateAvatarCommandConverter updateAvatarCommandConverter;
//  private final UserProfileDTO.DeleteAvatarCommandConverter deleteAvatarCommandConverter;


  public UserProfileModule(ChatSessionExecutor chatSessionExecutor) {
    this.chatSessionExecutor = chatSessionExecutor;
    this.chatSessionController = chatSessionExecutor.getChatSessionController();
    this.converter = (UserProfileDTOConverter) chatSessionExecutor.getDTOConverterManager().getConverterBySection(RequestDTO.DTO_SECTION.USERPROFILE);
//    this.updateAvatarCommandConverter = converter.getUpdateAvatarCommandConverter();
//    this.deleteAvatarCommandConverter = converter.getDeleteAvatarCommandConverter();
  }

  public void updateAvatarAction(File selectedFile) {
    chatSessionExecutor.executeModuleAction(() -> {
      try {
        var conv = converter.getUpdateAvatarCommandConverter();
        byte[] imageBytes = Files.readAllBytes(selectedFile.toPath());
        String mimeType = Files.probeContentType(selectedFile.toPath());
        chatSessionExecutor.getIOProcessor().sendMessage(conv.serialize(new UpdateAvatarCommand(mimeType, imageBytes.length, imageBytes)).getBytes());
        updateAvatarResponse(imageBytes);
      } catch (UnableToSerialize e) {
        log.info(e.getMessage(), e);
      } catch (IOException e) {
        try {
          chatSessionExecutor.shutdownConnection();
        } catch (IOException _) {
        }
//        todo remove from here
        log.info(e.getMessage(), e);
      }
    });
  }

  private void updateAvatarResponse(byte[] imageBytes) {
    chatSessionExecutor.executeModuleAction(() -> {
      try {
        Document root = chatSessionExecutor.getModuleExchanger().take();
        var upconf = converter.getUpdateAvatarCommandConverter();
        DTOInterfaces.RESPONSE_DTO response = (DTOInterfaces.RESPONSE_DTO) upconf.deserialize(root);
        chatSessionController.onUpdateAvatar(imageBytes, response);
      } catch (UnableToDeserialize e) {
        log.error(e.getMessage());
      } catch (InterruptedException _) {
      }
    });
  }

  public void deleteAvatarAction() {
    chatSessionExecutor.executeModuleAction(() -> {
      var ioProcessor = chatSessionExecutor.getIOProcessor();
      var deleteConverter = converter.getDeleteAvatarCommandConverter();
      try {
        ioProcessor.sendMessage(deleteConverter.serialize(new DeleteAvatarCommand()).getBytes());
        log.info("perform delete avatar command");
        onDeleteAvatarResponse();
      } catch (UnableToSerialize e) {
        log.warn(e.getMessage(), e);
      } catch (IOException e) {
        try {
          chatSessionExecutor.shutdownConnection();
        } catch (IOException _) {
        }
      }
    });
  }

  private void onDeleteAvatarResponse() {
    chatSessionExecutor.executeModuleAction(() -> {
      try {
        var root = chatSessionExecutor.getModuleExchanger().take();
        var deleteConverter = converter.getDeleteAvatarCommandConverter();
        var response = (DTOInterfaces.RESPONSE_DTO) deleteConverter.deserialize(root);
        chatSessionController.onDeleteAvatar(response);
      } catch (UnableToDeserialize e) {
        log.error(e.getMessage(), e);
      } catch (InterruptedException _) {
      }
    });
  }

  @Override
  public void eventAction(Document root) {
    var eventType = DTOConverterManagerInterface.getDTOEvent(root);
    try {
      if (eventType == RequestDTO.EVENT_TYPE.UPDATEAVATAR) {
        var event = converter.getUpdateAvatarCommandConverter().deserialize(root);
        chatSessionController.onUpdateAvatarEvent((UpdateAvatarEvent) event);
      } else if (eventType == RequestDTO.EVENT_TYPE.DELETEAVATAR) {
        var event = converter.getDeleteAvatarCommandConverter().deserialize(root);
        chatSessionController.onDeleteAvatarEvent((dto.subtypes.user_profile.DeleteAvatarEvent) event);
      } else {
        log.error(DTOConverterManagerInterface.getSTRDTOEvent(root));
      }
    } catch (UnableToDeserialize e) {
      log.error(e.getMessage(), e);
    }
  }
}
