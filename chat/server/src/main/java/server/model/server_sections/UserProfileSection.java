package server.model.server_sections;

import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.interfaces.DTOConverterManagerInterface;
import dto.subtypes.UserProfileDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import server.model.Server;
import server.model.connection_section.ChatUsersDAO;
import server.model.connection_section.ServerConnection;
import server.model.server_sections.interfaces.AbstractSection;

import java.io.IOException;

import static dto.RequestDTO.COMMAND_TYPE.DELETEAVATAR;
import static dto.RequestDTO.COMMAND_TYPE.UPDATEAVATAR;

public class UserProfileSection implements AbstractSection {
  private static final Logger log = LoggerFactory.getLogger(UserProfileSection.class);
  private final Server server;
  private final UserProfileDTO.UpdateAvatarCommandConverter updateAvatarCommandConverter;
  private final UserProfileDTO.DeleteAvatarCommandConverter deleteAvatarCommandConverter;
  private final ChatUsersDAO chatUsersDAO;

  public UserProfileSection(Server server) {
    this.server = server;
    var conv = (UserProfileDTO.UserProfileDTOConverter) server.getConverterManager().getConverterBySection(RequestDTO.DTO_SECTION.USERPROFILE);
    updateAvatarCommandConverter = conv.getUpdateAvatarCommandConverter();
    deleteAvatarCommandConverter = conv.getDeleteAvatarCommandConverter();
    chatUsersDAO = server.getChatUsersDAO();
  }

  @Override
  public void perform(ServerConnection connection, Document root, RequestDTO.DTO_TYPE type, RequestDTO.DTO_SECTION section) throws IOException {
    RequestDTO.COMMAND_TYPE commandType;
    if (type != RequestDTO.DTO_TYPE.COMMAND || (commandType = DTOConverterManagerInterface.getDTOCommand(root)) == null) {
      try {
        connection.sendMessage(deleteAvatarCommandConverter.serialize(new UserProfileDTO.UserProfileCommandError(STR."support only COMMAND: \{DELETEAVATAR}/\{UPDATEAVATAR}")).getBytes());
      } catch (IOException e) {
        log.error(e.getMessage(), e);
        server.submitExpiredConnection(connection);
      }
      return;
    }
    try {
      if (commandType == UPDATEAVATAR) {
        onUpdateAvatar(connection, (UserProfileDTO.UpdateAvatarCommand) updateAvatarCommandConverter.deserialize(root));
      } else if (commandType == DELETEAVATAR) {
        onDeleteAvatar(connection, (UserProfileDTO.DeleteAvatarCommand) deleteAvatarCommandConverter.deserialize(root));
      } else {
        connection.sendMessage(deleteAvatarCommandConverter.serialize(
                new UserProfileDTO.UserProfileCommandError(
                        STR."unsupported command type \{DTOConverterManagerInterface.getSTRDTOCommand(root)}"
                )
        ).getBytes());
      }
    } catch (UnableToDeserialize | UnableToSerialize e) {
      onUnableToSerializeEx(connection, e);
    }
  }

  private void onUpdateAvatar(ServerConnection connection, UserProfileDTO.UpdateAvatarCommand updateAvatarCommand) {
    try {
      String status = chatUsersDAO.updateProfilePictureByUserName(connection.getConnectionName(), updateAvatarCommand.getMimeType(), updateAvatarCommand.getContent());
      if (status == null) {
        connection.sendMessage(updateAvatarCommandConverter.serialize(new UserProfileDTO.UpdateAvatarCommandSuccess()).getBytes());
        try {
          byte[] msg = updateAvatarCommandConverter.serialize(
                  new UserProfileDTO.UpdateAvatarEvent(
                          connection.getConnectionName(),
                          updateAvatarCommand.getMimeType(),
                          updateAvatarCommand.getContent()
                  )
          ).getBytes();
          connection.sendBroadcastMessage(server, msg, log);
        } catch (IOException e) {
          log.error(e.getMessage(), e);
        }
      } else {
        connection.sendMessage(updateAvatarCommandConverter.serialize(
                new UserProfileDTO.UserProfileCommandError(status)
        ).getBytes());
      }
    } catch (UnableToSerialize e) {
      onUnableToSerializeEx(connection, e);
    } catch (IOException e) {
      log.info(e.getMessage(), e);
      server.submitExpiredConnection(connection);
    }
  }

  private void onDeleteAvatar(ServerConnection connection, UserProfileDTO.DeleteAvatarCommand deleteAvatarCommand) {
    try {
      String status = chatUsersDAO.deleteProfilePictureByUserName(connection.getConnectionName());
      if (status == null) {
        connection.sendMessage(deleteAvatarCommandConverter.serialize(
                new UserProfileDTO.DeleteAvatarCommandSuccess()
        ).getBytes());
        try {
          byte[] msg = deleteAvatarCommandConverter.serialize(
                  new UserProfileDTO.DeleteAvatarEvent(connection.getConnectionName())
          ).getBytes();
          connection.sendBroadcastMessage(server, msg, log);
        } catch (IOException e) {
          log.error(e.getMessage(), e);
        }
      } else {
        connection.sendMessage(deleteAvatarCommandConverter.serialize(
                new UserProfileDTO.UserProfileCommandError(status)
        ).getBytes());
      }
    } catch (UnableToSerialize e) {
      onUnableToSerializeEx(connection, e);
    } catch (IOException e) {
      log.info(e.getMessage(), e);
      server.submitExpiredConnection(connection);
    }
  }


  private void onUnableToSerializeEx(ServerConnection connection, Throwable e) {
    try {
      connection.sendMessage(updateAvatarCommandConverter.serialize(new UserProfileDTO.UserProfileCommandError(e.getMessage())).getBytes());
    } catch (IOException e1) {
      log.error(e1.getMessage(), e1);
      server.submitExpiredConnection(connection);
    }
  }
}
