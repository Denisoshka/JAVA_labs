package client.view.chat_session;

import client.facade.ChatSessionController;
import client.view.ControllerIntroduce;
import client.view.chat_session.events.ChatRecord;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class ChatSession extends VBox implements ControllerIntroduce {
  private static final int OTHERS_COLUMN_INDEX = 0;
  private static final int EVENT_COLUMN_INDEX = 1;
  private static final int USER_COLUMN_INDEX = 2;
  private static final Logger log = LoggerFactory.getLogger(ChatSession.class);

  @FXML
  private Button fileChoseWindowButton;
  @FXML
  private TextField messageTextField;
  @FXML
  private Button sendMessageButton;
  @FXML
  private ScrollPane chatScrollPane;
  @FXML
  private GridPane chatGridPane;
  @FXML
  private Button selectFileButton;

  private ChatSessionController chatSessionController;
  private Stage fileChoseWindowStage;

  public enum ChatEventType {
    SEND,
    RECEIVE,
    EVENT,
    ;
  }

  public ChatSession() {
    FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("ChatSession.fxml"));
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
    sendMessageButton.setOnAction(this::sendMessage);
    selectFileButton.setOnAction(this::selectFile);
    fileChoseWindowButton.setOnAction(this::showFileChoseWindow);
    chatGridPane.heightProperty().addListener((_, _, _) -> chatScrollPane.setVvalue(1.0));
  }

  @Override
  public void setChatSessionController(ChatSessionController chatSessionController) {
    this.chatSessionController = chatSessionController;
  }

  @FXML
  private void sendMessage(ActionEvent actionEvent) {
    log.info("Sending chat message");
    chatSessionController.messageCommand(messageTextField.getText());
    messageTextField.clear();
  }

  @FXML
  private void selectFile(ActionEvent actionEvent) {
    log.info("Selecting file");
    FileChooser fileChooser = new FileChooser();
    File selectedFile = fileChooser.showOpenDialog(this.getScene().getWindow());
    if (selectedFile != null) {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.initModality(Modality.APPLICATION_MODAL);
      alert.initOwner(this.getScene().getWindow());
      alert.setTitle("Confirmation Dialog");
      alert.setContentText(STR." send file: \{selectedFile.getName()}");

      ButtonType sendButtonType = new ButtonType("Send", ButtonBar.ButtonData.OK_DONE);
      ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
      alert.getButtonTypes().setAll(sendButtonType, cancelButtonType);
      Optional<ButtonType> result = alert.showAndWait();
      if (result.isPresent() && result.get() == sendButtonType) {
        chatSessionController.uploadFile(selectedFile);
      }
    }
  }

  private void showFileChoseWindow(Event event) {
    log.info("Showing file chose window");
    if (fileChoseWindowStage == null) {
      fileChoseWindowStage = new Stage();
      fileChoseWindowStage.initModality(Modality.APPLICATION_MODAL);
      fileChoseWindowStage.initOwner(this.getScene().getWindow());
      Scene scene = new Scene(new FileChooseWindow(chatSessionController));
      fileChoseWindowStage.setScene(scene);
    }
    fileChoseWindowStage.showAndWait();
  }

  public <T extends Node & ChatRecord> void addNewChatRecord(T chatRecord) {
    ChatEventType type = chatRecord.getType();
    final var columnIndex = type == ChatEventType.EVENT ? EVENT_COLUMN_INDEX :
            (type == ChatEventType.SEND ? USER_COLUMN_INDEX : OTHERS_COLUMN_INDEX);
    final var columnSpan = 2;
    final var rowIndex = chatGridPane.getRowCount();
    final var rowSpan = 1;
    Platform.runLater(() -> chatGridPane.add(chatRecord, columnIndex, rowIndex, columnSpan, rowSpan));
  }


  public void clearSession() {
    Platform.runLater(() -> chatGridPane.getChildren().clear());
  }
}
