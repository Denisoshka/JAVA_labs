package client.facade;

import client.model.chat_modules.submodules.*;
import client.model.main_context.ChatSessionExecutor;
import client.model.main_context.interfaces.ConnectionModule;
import client.view.ChatSessionView;
import client.view.ChatUsersInfo;
import client.view.SessionInfoBlock;
import client.view.chat_session.ChatSession;
import client.view.chat_session.events.*;
import dto.DataDTO;
import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import dto.subtypes.*;
import javafx.scene.image.Image;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class ChatSessionController {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(ChatSessionController.class);


  private MessageModule messageModule;
  private LoginModule loginModule;
  private LogoutModule logoutModule;
  private ListModule listModule;
  private UserProfileModule userProfileModule;
  private ChatSession chatSession;
  private SessionInfoBlock sessionInfoBlock;
  private ChatUsersInfo chatUsersInfo;
  private FileModule fileModule;
  private final ConcurrentMap<String, Image> userProfileImages = new ConcurrentHashMap<>();


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
    messageModule.commandAction(new MessageDTO.Command(message), null);
  }


  public void loginCommand(String login, String password, String hostname, int port) {
    loginModule.commandAction(null, new DataDTO.LoginData(login, hostname, password, port));
  }


  public void onLoginCommand(RequestDTO.RESPONSE_TYPE responseType) {
    if (responseType != RequestDTO.RESPONSE_TYPE.SUCCESS) {
      return;
    }
    sessionInfoBlock.setConnectionStatus(ConnectionModule.ConnectionState.CONNECTED);
    reloadUsers();
  }


  public void logoutCommand() {
    logoutModule.commandAction(null, null);
  }


  public void onLogoutCommand(DTOInterfaces.RESPONSE_DTO response) {
    chatSession.clearSession();
    sessionInfoBlock.setConnectionStatus(ConnectionModule.ConnectionState.DISCONNECTED);
  }

  public void onConnectResponse(ConnectionModule.ConnectionState state) {
    sessionInfoBlock.setConnectionStatus(state);
  }


  public void reloadUsers() {
    listModule.commandAction(null, null);
  }


  public void onListResponse(ListDTO.Command command, DTOInterfaces.RESPONSE_DTO response) {
    if (response.getResponseType() != RequestDTO.RESPONSE_TYPE.SUCCESS) {
      return;
    }
    ListDTO.Success successResponse = (ListDTO.Success) response;
    ArrayList<UserInfo> usersInfo = new ArrayList<>(successResponse.getUsers().size());
    for (var info : successResponse.getUsers()) {
      usersInfo.add(new UserInfo(info.getName()));
    }
    chatUsersInfo.onReloadUsers(usersInfo);
  }


  public void onMessageResponse(MessageDTO.Command message, DTOInterfaces.RESPONSE_DTO response) {
    /*todo make for server sender desc and time*/
    /*todo remove this on release*/
    chatSession.addNewChatRecord(new ChatMessage(
            ChatSession.ChatEventType.SEND, "you =)",
            message.getMessage(), ZonedDateTime.now()
    ));
  }


  public void onMessageEvent(MessageDTO.Event event) {
    /*todo make for server sender desc and time*/
    chatSession.addNewChatRecord(new ChatMessage(
            ChatSession.ChatEventType.RECEIVE, event.getFrom(),
            event.getMessage(), ZonedDateTime.now()
    ));
  }


  public void onLogoutEvent(LogoutDTO.Event event) {
    /*todo make for server sender desc and time*/
    chatSession.addNewChatRecord(new LogoutEvent(event.getName(), ZonedDateTime.now()));
    chatUsersInfo.removeUser(new UserInfo(event.getName()));
  }


  public void onLoginEvent(LoginDTO.Event event) {
    /*todo make for server sender desc and time*/
    chatSession.addNewChatRecord(new LoginEvent(event.getName(), ZonedDateTime.now()));
    chatUsersInfo.addUser(new UserInfo(event.getName()));
  }

  public void uploadFile(File file) {
    fileModule.uploadAction(file.toPath());
  }

  public void downloadFile(Long fileId) {
    log.info(STR."Download request of \{fileId}");
    fileModule.downloadAction(new FileDTO.DownloadCommand(fileId));
  }


  public void onFileUploadResponse(FileDTO.Event event) {
//    todo unused
    addFileEvent(ChatSession.ChatEventType.SEND, event);
//    addFilePreview(event);
  }


  public void onFileUploadEvent(FileDTO.Event event) {
    addFileEvent(ChatSession.ChatEventType.EVENT, event);
//    addFilePreview(event);
  }


  private void addFileEvent(ChatSession.ChatEventType eventType, FileDTO.Event event) {
    chatSession.addNewChatRecord(new FileEvent(
            this, eventType, event.getId(),
            event.getFrom(), event.getName(), event.getSize(),
            event.getMimeType(), ZonedDateTime.now()
    ));
  }


  private void addFilePreview(FileDTO.Event event) {
    /*chatSession.()
            .onFileUpload(new FileMetadata(String.valueOf(event.getId()), event.getName(),
                    (int) event.getSize(), event.getMimeType())
            );*/
  }


  public void onListFileResponse(List<FileDTO.FileEntity> files) {
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


  public void onUpdateAvatar(byte[] imageBytes, DTOInterfaces.RESPONSE_DTO responseDto) {
    if (responseDto.getResponseType() == RequestDTO.RESPONSE_TYPE.SUCCESS) {
      sessionInfoBlock.onUpdateAvatar(null);
    } else {
      sessionInfoBlock.onUpdateAvatar(new Image(new ByteArrayInputStream(imageBytes)));
    }
  }


  public void deleteAvatar() {
    userProfileModule.deleteAvatarAction();
  }


  public void onDeleteAvatar(DTOInterfaces.RESPONSE_DTO responseDto) {
    if (responseDto.getResponseType() == RequestDTO.RESPONSE_TYPE.SUCCESS) {
      sessionInfoBlock.onDeleteAvatar();
    }
  }

  public void onUpdateAvatarEvent(UserProfileDTO.UpdateAvatarEvent event) {
    userProfileImages.put(event.getName(), new Image(new ByteArrayInputStream(event.getContent())));
  }

  public void onDeleteAvatarEvent(UserProfileDTO.UpdateAvatarEvent event) {
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
  }
}
