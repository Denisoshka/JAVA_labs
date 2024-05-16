package client.facade;

import client.model.chat_modules.submodules.ListModule;
import client.model.chat_modules.submodules.LoginModule;
import client.model.chat_modules.submodules.LogoutModule;
import client.model.chat_modules.submodules.MessageModule;
import client.model.main_context.ChatSessionExecutor;
import client.model.main_context.interfaces.ConnectionModule;
import client.view.ChatSession;
import client.view.ChatSessionView;
import client.view.ChatUsersInfo;
import client.view.RegistrationBlock;
import dto.DataDTO;
import dto.RequestDTO;
import dto.subtypes.ListDTO;
import dto.subtypes.LoginDTO;
import dto.subtypes.LogoutDTO;
import dto.subtypes.MessageDTO;
import lombok.extern.slf4j.Slf4j;

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

  public void setChatSessionExecutorDependence(ChatSessionExecutor chatSessionExecutor) {
    messageModule = (MessageModule) chatSessionExecutor.getChatModule(RequestDTO.DTO_SECTION.MESSAGE);
    logoutModule = (LogoutModule) chatSessionExecutor.getChatModule(RequestDTO.DTO_SECTION.LOGOUT);
    loginModule = (LoginModule) chatSessionExecutor.getChatModule(RequestDTO.DTO_SECTION.LOGIN);
    listModule = (ListModule) chatSessionExecutor.getChatModule(RequestDTO.DTO_SECTION.LIST);
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

  public void logoutCommand() {
    logoutModule.commandAction(null, null);
  }

  public void onMessageResponse(MessageDTO.Command message, RequestDTO.BaseResponse response) {
    chatSession.addNewChatRecord(
            /*todo make for server sender desc and time*/
            new ChatSession.ChatRecord(
                    ChatSession.ChatEventType.RECEIVE, "AB xyi AV xyi",
//   todo remove this on release
                    message.getMessage(), ZonedDateTime.now()
            )
    );
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

  public void onMessageEvent(MessageDTO.Event event) {
    chatSession.addNewChatRecord(
            /*todo make for server sender desc and time*/
            new ChatSession.ChatRecord(
                    ChatSession.ChatEventType.RECEIVE, event.getFrom(),
                    event.getMessage(), ZonedDateTime.now()
            )
    );
  }

  public void onLogoutEvent(LogoutDTO.Event event) {
    chatSession.addNewChatRecord(
            /*todo make for server sender desc and time*/
            new ChatSession.ChatRecord(
                    ChatSession.ChatEventType.EVENT, "",
                    STR."logout user: \{event.getName()}",
                    ZonedDateTime.now()
            )
    );
  }

  public void onLoginEvent(LoginDTO.Event event) {
    chatSession.addNewChatRecord(
            /*todo make for server sender desc and time*/
            new ChatSession.ChatRecord(
                    ChatSession.ChatEventType.EVENT, "",
                    STR."login user: \{event.getName()}",
                    ZonedDateTime.now()
            )
    );
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
