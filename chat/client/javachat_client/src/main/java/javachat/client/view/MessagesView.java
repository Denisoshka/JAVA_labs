package javachat.client.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

import java.io.IOException;

public class MessagesView {
  public static class TextMessage {
    @FXML
    private Label username;
    @FXML
    private Label messageText;
    TextMessage(String user, String message) {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ChatMessage.fxml"));
      this.username.setText(user);
      this.messageText.setText(message);
      fxmlLoader.setRoot(this);
      fxmlLoader.setController(this);
      try {
        fxmlLoader.load();
      } catch (IOException exception) {
        throw new RuntimeException(exception);
      }
    }
  }
}
