package javachat.client.view;

import javachat.client.facade.ChatSessionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class RegistrationBlock extends VBox implements ControllerIntroduce {
  @FXML
  private TextField chatName;
  @FXML
  private TextField userLogin;
  @FXML
  private PasswordField userPassword;
  @FXML
  private Button login;
  @FXML
  private Button logout;

  ChatSessionController controller;

  public RegistrationBlock() {
    FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("RegistrationBlock.fxml"));
    fxmlloader.setRoot(this);
    fxmlloader.setController(this);
    try {
      fxmlloader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  @FXML
  public void performLogin() {
//    controller.login(null, null, null, null);
  }

  @FXML
  public void performLogout() {

  }

  @Override
  public void setController(ChatSessionController controller) {
    this.controller = controller;
  }

  public TextField getChatName() {
    return chatName;
  }

  public void setChatName(TextField chatName) {
    this.chatName = chatName;
  }

  public TextField getUserLogin() {
    return userLogin;
  }

  public void setUserLogin(TextField userLogin) {
    this.userLogin = userLogin;
  }

  public PasswordField getUserPassword() {
    return userPassword;
  }

  public void setUserPassword(PasswordField userPassword) {
    this.userPassword = userPassword;
  }

}
