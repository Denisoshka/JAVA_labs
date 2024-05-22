package client.facade;

import client.model.chat_modules.submodules.*;
import client.model.main_context.ChatSessionExecutor;
import client.model.main_context.interfaces.ConnectionModule;
import client.view.ChatSessionView;
import client.view.ChatUsersInfo;
import client.view.RegistrationBlock;
import client.view.chat_session.ChatSession;
import client.view.chat_session.events.ChatMessage;
import client.view.chat_session.events.FileEvent;
import client.view.chat_session.events.LoginEvent;
import client.view.chat_session.events.LogoutEvent;
import dto.DataDTO;
import dto.RequestDTO;
import dto.subtypes.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ChatSessionController {
  private static final String USER_LOGIN = "userlogin";
  private static final String USER_LOGOUT = "userlogout";
  private static final String USER_MESSAGE = "message";

  private MessageModule messageModule;
  private LoginModule loginModule;
  private LogoutModule logoutModule;
  private ListModule listModule;
  private ChatSession chatSession;
  private RegistrationBlock registrationBlock;
  private ChatUsersInfo chatUsersInfo;
  private FileModule fileModule;

  public void setChatSessionExecutorDependence(ChatSessionExecutor chatSessionExecutor) {
    messageModule = (MessageModule) chatSessionExecutor.getChatModule(RequestDTO.DTO_SECTION.MESSAGE);
    logoutModule = (LogoutModule) chatSessionExecutor.getChatModule(RequestDTO.DTO_SECTION.LOGOUT);
    loginModule = (LoginModule) chatSessionExecutor.getChatModule(RequestDTO.DTO_SECTION.LOGIN);
    listModule = (ListModule) chatSessionExecutor.getChatModule(RequestDTO.DTO_SECTION.LIST);
    fileModule = (FileModule) chatSessionExecutor.getChatModule(RequestDTO.DTO_SECTION.FILE);
  }


  public void setChatSessionViewDependence(ChatSessionView chatSessionView) {
    this.chatSession = chatSessionView.getChatSession();
    this.registrationBlock = chatSessionView.getRegistrationBlock();
    this.chatUsersInfo = chatSessionView.getChatUsersInfo();
  }

  public void showMessage(RequestDTO requestDTO, RequestDTO.BaseResponse response) {
    if (requestDTO.getDTOType() == RequestDTO.DTO_TYPE.EVENT) {
      onEvent((RequestDTO.BaseEvent) requestDTO);
    } else if (requestDTO.getDTOType() == RequestDTO.DTO_TYPE.COMMAND) {
      onCommand((RequestDTO.BaseCommand) requestDTO, response);
    }
  }

  private void onEvent(RequestDTO.BaseEvent event) {
    switch (event.getNameAttribute()) {
      case USER_MESSAGE -> onMessageEvent((MessageDTO.Event) event);
      case USER_LOGIN -> onLoginEvent((LoginDTO.Event) event);
      case USER_LOGOUT -> onLogoutEvent((LogoutDTO.Event) event);
    }
  }

  private void onCommand(RequestDTO.BaseCommand event, RequestDTO.BaseResponse response) {
    switch (event.geDTOSection()) {
      case MESSAGE -> onMessageResponse((MessageDTO.Command) event, null);
    }
  }

  public void messageCommand(String message) {
    messageModule.commandAction(new MessageDTO.Command(message), null);
  }

  public void loginCommand(String login, String password, String hostname, int port) {
    loginModule.commandAction(null, List.of(new DataDTO.LoginData(login, hostname, password, port)));
  }

  public void onLoginCommand(RequestDTO.BaseResponse.RESPONSE_TYPE responseType) {
    if (responseType != RequestDTO.BaseResponse.RESPONSE_TYPE.SUCCESS) {
      return;
    }
    registrationBlock.setConnectionStatus(ConnectionModule.ConnectionState.CONNECTED);
    reloadUsers();
  }

  public void logoutCommand() {
    logoutModule.commandAction(null, null);
  }

  public void onLogoutCommand(RequestDTO.BaseResponse response) {
    chatSession.clearSession();
    registrationBlock.setConnectionStatus(ConnectionModule.ConnectionState.DISCONNECTED);
  }

  public void onConnectResponse(ConnectionModule.ConnectionState state) {
    registrationBlock.setConnectionStatus(state);
  }

  public void reloadUsers() {
    listModule.commandAction(null, null);
  }

  public void onListResponse(ListDTO.Command command, RequestDTO.BaseResponse response) {
    if (response.getResponseType() != RequestDTO.BaseResponse.RESPONSE_TYPE.SUCCESS) {
      return;
    }
    ListDTO.Success successResponse = (ListDTO.Success) response;
    ArrayList<UserInfo> usersInfo = new ArrayList<>(successResponse.getUsers().size());
    for (var info : successResponse.getUsers()) {
      usersInfo.add(new UserInfo(info.getName()));
    }
    chatUsersInfo.onReloadUsers(usersInfo);
  }

  public void onMessageResponse(MessageDTO.Command message, RequestDTO.BaseResponse response) {
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

  public void downloadFile(String fileId) {
    fileModule.downloadAction(new FileDTO.DownloadCommand(fileId));
  }

  public void onFileUploadResponse(FileDTO.Event event) {
    addFileEvent(ChatSession.ChatEventType.SEND, event);
    addFilePreview(event);
  }

  public void onFileUploadEvent(FileDTO.Event event) {
    addFileEvent(ChatSession.ChatEventType.RECEIVE, event);
    addFilePreview(event);
  }

  private void addFileEvent(ChatSession.ChatEventType eventType, FileDTO.Event event) {
    chatSession.addNewChatRecord(new FileEvent(
            eventType, event.getId(), event.getFrom(),
            event.getName(), event.getSize(),
            event.getMimeType(), ZonedDateTime.now()
    ));
  }

  private void addFilePreview(FileDTO.Event event) {

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
