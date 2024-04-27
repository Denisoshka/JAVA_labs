package javachat.client.view;

import javachat.client.facade.ChatSessionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ConnectedUsers extends VBox implements ControllerIntroduce {
  @FXML
  ListView<String> connectedUsers;

  ChatSessionController controller;

  public ConnectedUsers() {
    FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("ConnectedUsers.fxml"));
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
    this.controller = controller;
  }
}
