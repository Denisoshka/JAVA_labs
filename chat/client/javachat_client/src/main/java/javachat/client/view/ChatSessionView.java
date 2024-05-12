package javachat.client.view;

import javachat.client.facade.ChatSessionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ChatSessionView extends Pane {
  @FXML
  private ChatSession chatSession;

  @FXML
  private RegistrationBlock registrationBlock;
  @FXML
  private ConnectedUsers connectedUsers;
  private ChatSessionController chatSessionController;

  public ChatSessionView() {
    FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("ChatView.fxml"));
    fxmlloader.setRoot(this);
//    fxmlloader.setController(this);
    try {
      fxmlloader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  public ChatSession getChatSession() {
    return chatSession;
  }
/*
  public void onNewUserLogout(String user) {
    Platform.runLater(() -> connectedUsers.removeUser(user));
  }

  public void onNewUserLogin(String user) {
    Platform.runLater(() -> connectedUsers.addUser(user));
  }

  public void onCharLogin() {
  }*/
}
