package client.view;

import client.facade.ChatSessionController;
import client.model.main_context.interfaces.ConnectionModule;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.slf4j.Logger;

import java.io.IOException;

public class RegistrationBlock extends VBox implements ControllerIntroduce {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(RegistrationBlock.class);
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
  @FXML
  private TextField connectionStatus;
  @FXML
  private Circle connectionStatusMarker;
  @FXML
  private HBox connectionStatusBox;
  @FXML
  private Label connectionStatusLabel;

  public enum ConnectionStatus {
    Connected(Color.GREEN),
    Disconnected(Color.RED),
    ;

    private final Color statusColor;

    ConnectionStatus(Color color) {
      statusColor = color;
    }
  }

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

  @FXML
  public void initialize() {
    login.setOnAction(this::performLogin);
    logout.setOnAction(this::performLogout);
    updateStatus(ConnectionStatus.Disconnected);
    hostname.setPromptText("Hostname");
    port.setPromptText("Port");
    userLogin.setPromptText("Login");
    userPassword.setPromptText("Password");
    port.setText("9876");
    hostname.setText("localhost");
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

  public void setConnectionStatus(ConnectionModule.ConnectionState connectionState) {
    if (connectionState == ConnectionModule.ConnectionState.CONNECTED) {
      updateStatus(ConnectionStatus.Connected);
    } else {
      updateStatus(ConnectionStatus.Disconnected);
    }
  }

  private void updateStatus(ConnectionStatus status) {
    Platform.runLater(() -> {
      connectionStatusLabel.textProperty().setValue(status.toString());
      connectionStatusMarker.setFill(status.statusColor);
    });
  }

  private void setDisconnected() {
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
