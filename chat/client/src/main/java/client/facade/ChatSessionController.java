package client.facade;

import client.model.chat_modules.submodules.*;
import client.model.main_context.ChatSessionExecutor;
import client.model.main_context.interfaces.ConnectionModule;
import client.view.ChatSessionView;
import client.view.ChatUsersInfo;
import client.view.SessionInfoBlock;
import client.view.chat_session.ChatSession;
import client.view.chat_session.events.*;
import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import dto.subtypes.file.FileDownloadCommand;
import dto.subtypes.file.FileEntity;
import dto.subtypes.list.ListSuccess;
import dto.subtypes.login.LoginSuccess;
import dto.subtypes.message.MessageCommand;
import dto.subtypes.message.MessageEvent;
import dto.subtypes.other.LoginData;
import dto.subtypes.user_profile.DeleteAvatarEvent;
import dto.subtypes.user_profile.UpdateAvatarEvent;
import javafx.scene.image.Image;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class ChatSessionController {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(ChatSessionController.class);

  private final ConcurrentMap<String, Image> userProfileImages = new ConcurrentHashMap<>();

  private MessageModule messageModule;
  private LoginModule loginModule;
  private LogoutModule logoutModule;
  private ListModule listModule;
  private UserProfileModule userProfileModule;
  private ChatSession chatSession;
  private SessionInfoBlock sessionInfoBlock;
  private ChatUsersInfo chatUsersInfo;
  private FileModule fileModule;

  public void setChatSessionExecutorDependence(ChatSessionExecutor chatSessionExecutor) {
    messageModule = (MessageModule) chatSessionExecutor.getChatModule(RequestDTO.DTO_SECTION.MESSAGE);
    logoutModule = (LogoutModule) chatSessionExecutor.getChatModule(RequestDTO.DTO_SECTION.LOGOUT);
    loginModule = (LoginModule) chatSessionExecutor.getChatModule(RequestDTO.DTO_SECTION.LOGIN);
    listModule = (ListModule) chatSessionExecutor.getChatModule(RequestDTO.DTO_SECTION.LIST);
    fileModule = (FileModule) chatSessionExecutor.getChatModule(RequestDTO.DTO_SECTION.FILE);
    userProfileModule = (UserProfileModule) chatSessionExecutor.getChatModule(RequestDTO.DTO_SECTION.USERPROFILE);
  }


  public void setChatSessionViewDependence(ChatSessionView chatSessionView) {
    this.chatSession = chatSessionView.getChatSession();
    this.sessionInfoBlock = chatSessionView.getRegistrationBlock();
    this.chatUsersInfo = chatSessionView.getChatUsersInfo();
  }


  public void messageCommand(String message) {
    messageModule.commandAction(new MessageCommand(message), null);
  }


  public void loginCommand(String login, String password, String hostname, int port) {
    loginModule.commandAction(new LoginData(login, hostname, password, port));
  }


  public void onLoginResponse(DTOInterfaces.RESPONSE_DTO response) {
    if (response.getResponseType() == RequestDTO.RESPONSE_TYPE.SUCCESS) {
      var sucresp = (LoginSuccess) response;
      sessionInfoBlock.setConnectionStatus(ConnectionModule.ConnectionState.CONNECTED);
      sessionInfoBlock.updateAvatar(new Image(new ByteArrayInputStream(sucresp.getContent())));
      reloadUsers();
    }
  }


  public void logoutCommand() {
    logoutModule.logoutAction();
  }


  public void onLogoutResponse(DTOInterfaces.RESPONSE_DTO response) {
    updateConnectionStatus(ConnectionModule.ConnectionState.DISCONNECTED);
    chatSession.clearSession();
    userProfileImages.clear();
    sessionInfoBlock.deleteAvatar();
  }

  public void updateConnectionStatus(ConnectionModule.ConnectionState state) {
    sessionInfoBlock.setConnectionStatus(state);
  }


  public void reloadUsers() {
    listModule.commandAction();
  }


  public void onListResponse(DTOInterfaces.RESPONSE_DTO response) {
    if (response.getResponseType() != RequestDTO.RESPONSE_TYPE.SUCCESS) {
      return;
    }

    ListSuccess successResponse = (ListSuccess) response;
    ArrayList<UserInfo> usersInfo = new ArrayList<>(successResponse.getUsers().size());
    userProfileImages.clear();
    for (var info : successResponse.getUsers()) {
      usersInfo.add(new UserInfo(info.getName()));
      if (info.getContent() != null)
        userProfileImages.put(info.getName(), new Image(new ByteArrayInputStream(info.getContent())));
    }
    chatUsersInfo.onReloadUsers(usersInfo);
  }


  public void onMessageResponse(MessageCommand message, DTOInterfaces.RESPONSE_DTO response) {
    /*todo make for server sender desc and time*/
    /*todo remove this on release*/
    chatSession.addNewChatRecord(new ChatMessage(
            ChatSession.ChatEventType.SEND, "you =)",
            message.getMessage(), ZonedDateTime.now()
    ));
  }


  public void onMessageEvent(MessageEvent messageEvent) {
    /*todo make for server sender desc and time*/
    Image avatar = userProfileImages.get(messageEvent.getFrom());
    var rez = (avatar == null)
            ? new ChatMessage(ChatSession.ChatEventType.RECEIVE, messageEvent.getFrom(),
            messageEvent.getMessage(), ZonedDateTime.now()
    )
            : new ChatMessage(ChatSession.ChatEventType.RECEIVE, avatar, messageEvent.getFrom(),
            messageEvent.getMessage(), ZonedDateTime.now()
    );
    chatSession.addNewChatRecord(rez);
  }


  public void onLogoutEvent(dto.subtypes.logout.LogoutEvent logoutEvent) {
    /*todo make for server sender desc and time*/
    chatSession.addNewChatRecord(new LogoutEvent(logoutEvent.getName(), ZonedDateTime.now()));
    chatUsersInfo.removeUser(new UserInfo(logoutEvent.getName()));
    userProfileImages.remove(logoutEvent.getName());
  }


  public void onLoginEvent(dto.subtypes.login.LoginEvent event) {
    /*todo make for server sender desc and time*/
    chatSession.addNewChatRecord(new LoginEvent(event.getName(), ZonedDateTime.now()));
    chatUsersInfo.addUser(new UserInfo(event.getName()));
  }


  public void uploadFile(File file) {
    fileModule.uploadAction(file.toPath());
  }


  public void downloadFile(Long fileId) {
    log.info(STR."Download request of \{fileId}");
    fileModule.downloadAction(new FileDownloadCommand(fileId));
  }


  public void onFileUploadResponse(dto.subtypes.file.FileEvent fileEvent) {
//    todo unused
    addFileEvent(ChatSession.ChatEventType.SEND, fileEvent);
//    addFilePreview(event);
  }


  public void onFileUploadEvent(dto.subtypes.file.FileEvent fileEvent) {
    addFileEvent(ChatSession.ChatEventType.EVENT, fileEvent);
//    addFilePreview(event);
  }


  private void addFileEvent(ChatSession.ChatEventType eventType, dto.subtypes.file.FileEvent fileEvent) {
    chatSession.addNewChatRecord(new FileEvent(
            this, eventType, fileEvent.getId(),
            fileEvent.getFrom(), fileEvent.getName(), fileEvent.getSize(),
            fileEvent.getMimeType(), ZonedDateTime.now()
    ));
  }


  private void addFilePreview(dto.subtypes.file.FileEvent fileEvent) {
    /*chatSession.()
            .onFileUpload(new FileMetadata(String.valueOf(event.getId()), event.getName(),
                    (int) event.getSize(), event.getMimeType())
            );*/
  }


  public void onListFileResponse(List<FileEntity> files) {
    ArrayList<FileMetadata> rez = new ArrayList<>(files.size());
    for (var file : files) {
      rez.add(new FileMetadata(String.valueOf(file.getId()), file.getName(), file.getSize(), file.getMimeType()));
    }
    chatSession.getFileChoseWindow().onListFiles(rez);
  }


  public void listFilesAction() {
    fileModule.fileListAction();
  }


  public void updateAvatar(File selectedFile) {
    userProfileModule.updateAvatarAction(selectedFile);
  }


  public void onUpdateAvatarResponse(byte[] imageBytes, DTOInterfaces.RESPONSE_DTO responseDto) {
    if (responseDto.getResponseType() == RequestDTO.RESPONSE_TYPE.SUCCESS) {
      sessionInfoBlock.updateAvatar(new Image(new ByteArrayInputStream(imageBytes)));
    }
  }


  public void deleteAvatar() {
    userProfileModule.deleteAvatarAction();
  }


  public void onDeleteAvatar(DTOInterfaces.RESPONSE_DTO responseDto) {
    log.debug(STR."RESPONSE_TYPE  \{responseDto.getResponseType()}");
    if (responseDto.getResponseType() == RequestDTO.RESPONSE_TYPE.SUCCESS) {
      sessionInfoBlock.deleteAvatar();
    } else if (responseDto.getResponseType() == RequestDTO.RESPONSE_TYPE.ERROR) {
      log.info((((DTOInterfaces.ERROR_RESPONSE_DTO) responseDto).getMessage()));
    }
  }

  public void onUpdateAvatarEvent(UpdateAvatarEvent event) {
    userProfileImages.put(event.getName(), new Image(new ByteArrayInputStream(event.getContent())));
  }

  public void onDeleteAvatarEvent(DeleteAvatarEvent event) {
    userProfileImages.remove(event.getName());
  }

  public static class UserInfo {
    private String name;

    public UserInfo(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof UserInfo userInfo)) return false;
      return Objects.equals(name, userInfo.name);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(name);
    }
  }
}
