package javachat.client.view;

import javachat.client.facade.ChatSessionController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class RegistrationBlock extends VBox implements ControllerIntroduce {
  @FXML
  private TextField hostname;
  @FXML
  private TextField port;
  @FXML
  private TextField userLogin;
  @FXML
  private PasswordField userPassword;
  @FXML
  private Button login;
  @FXML
  private Button logout;

  ChatSessionController chatSessionController;

  public RegistrationBlock() {
    FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("RegistrationBlock.fxml"));
    fxmlloader.setRoot(this);
    fxmlloader.setController(this);
    try {
      fxmlloader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
    initialize();
  }


  public void initialize() {
    login.setOnAction(this::performLogin);
    logout.setOnAction(this::performLogout);
    hostname.setPromptText("Hostname");
    port.setPromptText("Port");
    userLogin.setPromptText("Login");
    userPassword.setPromptText("Password");
  }

  @FXML
  public void performLogin(ActionEvent event) {
    try {
      log.info(STR."host: \{hostname.getText()}, port: \{Integer.parseInt(port.getText())}, login: \{userLogin.getText()}");
      chatSessionController.loginCommand(userLogin.getText(), userPassword.getText(), hostname.getText(), Integer.parseInt(port.getText()));
    } catch (NumberFormatException exception) {
      log.error(exception.getMessage());
    }
  }

  @FXML
  public void performLogout(ActionEvent event) {
    chatSessionController.logoutCommand();
  }

  @Override
  public void setChatSessionController(ChatSessionController chatSessionController) {
    this.chatSessionController = chatSessionController;
  }

  public TextField getHostname() {
    return hostname;
  }

  public void setHostname(TextField hostname) {
    this.hostname = hostname;
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
