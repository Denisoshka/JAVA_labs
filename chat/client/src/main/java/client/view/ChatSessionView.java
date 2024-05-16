package client.view;

import client.facade.ChatSessionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;

import java.io.IOException;

public class ChatSessionView extends SplitPane implements ControllerIntroduce {
  @FXML
  private ChatSession chatSession;
  @FXML
  private RegistrationBlock registrationBlock;
  @FXML
  private ChatUsersInfo chatUsersInfo;

  public ChatSessionView() {
    FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("ChatSessionView.fxml"));
    fxmlloader.setRoot(this);
    fxmlloader.setController(this);
    try {
      fxmlloader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  @Override
  public void setChatSessionController(ChatSessionController chatSessionController) {
    registrationBlock.setChatSessionController(chatSessionController);
    chatSession.setChatSessionController(chatSessionController);
    chatUsersInfo.setChatSessionController(chatSessionController);
  }

  public ChatSession getChatSession() {
    return chatSession;
  }

  public RegistrationBlock getRegistrationBlock() {
    return registrationBlock;
  }

  public ChatUsersInfo getChatUsersInfo() {
    return chatUsersInfo;
  }
}
