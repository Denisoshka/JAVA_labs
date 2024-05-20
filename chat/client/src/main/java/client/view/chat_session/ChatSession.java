package client.view.chat_session;

import client.facade.ChatSessionController;
import client.view.ControllerIntroduce;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ChatSession extends VBox implements ControllerIntroduce {
  private static final int OTHERS_COLUMN_INDEX = 0;
  private static final int EVENT_COLUMN_INDEX = 1;
  private static final int USER_COLUMN_INDEX = 2;
  private static final Logger log = LoggerFactory.getLogger(ChatSession.class);

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
  private File selectedFile;

  private ChatSessionController chatSessionController;

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

  public void initialize() {
    sendMessageButton.setOnAction(this::sendMessage);
    selectFileButton.setOnAction(this::selectFile);
    chatGridPane.heightProperty().addListener((_, _, _) -> chatScrollPane.setVvalue(1.0));
  }

  @Override
  public void setChatSessionController(ChatSessionController chatSessionController) {
    this.chatSessionController = chatSessionController;
  }

  @FXML
  private void sendMessage(ActionEvent actionEvent) {
    if (selectedFile == null) {
      log.info("Sending chat message");
      chatSessionController.messageCommand(messageTextField.getText());
      messageTextField.clear();
    } else {

    }
  }

  @FXML
  private void selectFile(ActionEvent actionEvent) {
    if (selectedFile == null) {
      log.info("Selecting file");
      FileChooser fileChooser = new FileChooser();
      File selectedFile = fileChooser.showOpenDialog(this.getScene().getWindow());
      if (selectedFile != null) {

//      todo implenent this;
//      selectedFileLabel.setText("Selected file: " + selectedFile.getName());
//      selectedFileLabel.setVisible(true);
//      messageField.setVisible(false);
      }
    } else {
      selectedFile = null;
    }
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
