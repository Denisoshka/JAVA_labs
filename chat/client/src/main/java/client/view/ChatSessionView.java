package client.view;

import client.facade.ChatSessionController;
import client.view.chat_session.ChatSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatSessionView extends SplitPane implements ControllerIntroduce {
  @FXML
  private ChatSession chatSession;
  @FXML
  private SessionInfoBlock sessionInfoBlock;
  @FXML
  private ChatUsersInfo chatUsersInfo;

  private final Stage primaryStage;

  public ChatSessionView(Stage primaryStage) {
    FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("ChatSessionView.fxml"));
    fxmlloader.setRoot(this);
    fxmlloader.setController(this);
    try {
      fxmlloader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
    this.primaryStage = primaryStage;
    this.chatSession.setPrimaryStage(primaryStage);
  }

  @Override
  public void setChatSessionController(ChatSessionController chatSessionController) {
    sessionInfoBlock.setChatSessionController(chatSessionController);
    chatSession.setChatSessionController(chatSessionController);
    chatUsersInfo.setChatSessionController(chatSessionController);
  }

  public ChatSession getChatSession() {
    return chatSession;
  }

  public SessionInfoBlock getRegistrationBlock() {
    return sessionInfoBlock;
  }

  public ChatUsersInfo getChatUsersInfo() {
    return chatUsersInfo;
  }
}
