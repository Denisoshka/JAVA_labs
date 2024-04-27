package javachat.client.view;

import javachat.client.facade.ChatSessionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ChatSessionView extends Pane implements ControllerIntroduce {
  @FXML
  private RegistrationBlock registrationBlock;
  @FXML
  private ConnectedUsers connectedUsers;
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
}
