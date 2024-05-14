package client.facade;

import client.model.chat_modules.submodules.LoginModule;
import client.model.chat_modules.submodules.LogoutModule;
import client.model.chat_modules.submodules.MessageModule;
import client.model.main_context.ChatSessionExecutor;
import client.view.ChatSession;
import client.view.ChatSessionView;
import dto.DataDTO;
import dto.RequestDTO;
import dto.subtypes.ListDTO;
import dto.subtypes.LoginDTO;
import dto.subtypes.LogoutDTO;
import dto.subtypes.MessageDTO;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class ChatSessionController {
  private static final String USER_LOGIN = "userlogin";
  private static final String USER_LOGOUT = "userlogout";
  private static final String USER_MESSAGE = "message";

  private ChatSession chatSession;
  private MessageModule messageModule;
  private LoginModule loginModule;
  private LogoutModule logoutModule;

  public void setChatSessionExecutorDependence(ChatSessionExecutor chatSessionExecutor) {
    messageModule = (MessageModule) chatSessionExecutor.getChatModule(RequestDTO.DTO_SECTION.MESSAGE);
    logoutModule = (LogoutModule) chatSessionExecutor.getChatModule(RequestDTO.DTO_SECTION.LOGOUT);
    loginModule = (LoginModule) chatSessionExecutor.getChatModule(RequestDTO.DTO_SECTION.LOGIN);
  }


  public void setChatSessionViewDependence(ChatSessionView chatSessionView) {
    this.chatSession = chatSessionView.getChatSession();
  }

  private ChatSessionExecutor chatSessionExecutor;

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
                    ChatSession.ChatEventType.RECEIVE,
//   todo remove this on release
                    "AB xyi AV xyi",
                    message.getMessage(),
                    ZonedDateTime.now()
            )
    );
  }

  public void onListResponse(ListDTO.Command command, RequestDTO.BaseResponse response) {
//    todo implements this section
  }

  public void onMessageEvent(MessageDTO.Event event) {
    chatSession.addNewChatRecord(
            /*todo make for server sender desc and time*/
            new ChatSession.ChatRecord(
                    ChatSession.ChatEventType.RECEIVE,
                    event.getFrom(),
                    event.getMessage(),
                    ZonedDateTime.now()
            )
    );
  }

  public void onLogoutEvent(LogoutDTO.Event event) {
    chatSession.addNewChatRecord(
            /*todo make for server sender desc and time*/
            new ChatSession.ChatRecord(
                    ChatSession.ChatEventType.EVENT,
                    "",
                    "logout user: " + event.getName(),
                    ZonedDateTime.now()
            )
    );
  }

  public void onLoginEvent(LoginDTO.Event event) {
    chatSession.addNewChatRecord(
            /*todo make for server sender desc and time*/
            new ChatSession.ChatRecord(
                    ChatSession.ChatEventType.EVENT,
                    "",
                    "login user: " + event.getName(),
                    ZonedDateTime.now()
            )
    );
  }

}
