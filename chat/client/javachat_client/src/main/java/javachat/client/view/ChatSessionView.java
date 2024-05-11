package javachat.client.view;

import javachat.client.facade.ChatSessionController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ChatSessionView extends Pane implements ControllerIntroduce {
  public ChatSession chatSession;
  public RegistrationBlock registrationBlock;
  public ConnectedUsers connectedUsers;
  ChatSessionController chatSessionController;

  public ChatSessionView() {
    FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("ChatView.fxml"));
    fxmlloader.setRoot(this);
    fxmlloader.setController(this);
    try {
      fxmlloader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  @Override
  public void setController(ChatSessionController controller) {
    this.chatSessionController = controller;
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
