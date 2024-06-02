package client.view;

import client.facade.ChatSessionController;
import client.model.main_context.interfaces.ConnectionModule;
import client.view.chat_session.interfaces.ControllerIntroduce;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class SessionInfoBlock extends VBox implements ControllerIntroduce {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(SessionInfoBlock.class);

  @FXML
  private ImageView profileAvatar;
  @FXML
  private Button updateAvatar;
  @FXML
  private Button deleteAvatar;
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
    CONNECTED(Color.GREEN),
    DISCONNECTED(Color.RED),
    ;

    private final Color statusColor;

    ConnectionStatus(Color color) {
      statusColor = color;
    }

    public Color getStatusColor() {
      return statusColor;
    }
  }

  ChatSessionController chatSessionController;

  public SessionInfoBlock() {
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
    updateAvatar.setOnAction(this::updateAvatarAction);
    deleteAvatar.setOnAction(this::deleteAvatarAction);
    updateStatus(ConnectionStatus.DISCONNECTED);
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
      updateStatus(ConnectionStatus.CONNECTED);
    } else {
      updateStatus(ConnectionStatus.DISCONNECTED);
    }
  }

  private void updateStatus(ConnectionStatus status) {
    Platform.runLater(() -> {
      connectionStatusLabel.textProperty().setValue(status.toString());
      connectionStatusMarker.setFill(status.statusColor);
    });
  }

  private void updateAvatarAction(Event event) {
    FileChooser fileChooser = new FileChooser();
    File selectedFile = fileChooser.showOpenDialog(this.getScene().getWindow());
    if (selectedFile != null) {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.initModality(Modality.APPLICATION_MODAL);
      alert.initOwner(this.getScene().getWindow());
      alert.setContentText(STR." set avatar: \{selectedFile.getName()}");

      ButtonType sendButtonType = new ButtonType("Send", ButtonBar.ButtonData.OK_DONE);
      ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
      alert.getButtonTypes().setAll(sendButtonType, cancelButtonType);
      Optional<ButtonType> result = alert.showAndWait();
      if (result.isPresent() && result.get() == sendButtonType) {
        chatSessionController.updateAvatar(selectedFile);
      }
    }
  }

  public void updateAvatar(Image image) {
    if (image != null) {
      Platform.runLater(() -> profileAvatar.setImage(image));
    }
  }

  private void deleteAvatarAction(Event event) {
    chatSessionController.deleteAvatar();
  }

  public void deleteAvatar() {
    Platform.runLater(() -> {
      profileAvatar.setImage(null);
    });
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
