package client.view.chat_session;

import client.facade.ChatSessionController;
import client.view.ControllerIntroduce;
import client.view.chat_session.events.FileMetadata;
import client.view.chat_session.events.FilePreviewCell;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

public class FileChooseWindow extends VBox implements ControllerIntroduce {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(FileChooseWindow.class);
  @FXML
  private ListView<FileMetadata> filePreviews;
  private ChatSessionController controller;
  private final Button listFiles = new Button();

  public FileChooseWindow() {
    FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("FileChoseWindow.fxml"));
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
  private void initialize() {
    ObservableList<FileMetadata> previews = FXCollections.observableArrayList();
    filePreviews.setItems(previews);
    filePreviews.setCellFactory(_ -> new ListCell<>() {
      @Override
      protected void updateItem(FileMetadata item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setGraphic(null);
        } else {
          setGraphic(new FilePreviewCell(controller, item));
        }
      }
    });
    listFiles.setOnAction(this::listFiles);
  }

  public void onListFiles(List<FileMetadata> files) {
    Platform.runLater(() -> {
      filePreviews.getItems().clear();
      filePreviews.getItems().addAll(files);
    });
  }

  public void listFiles(Event e) {
    controller.listFilesAction();
  }

  @Override
  public void setChatSessionController(ChatSessionController chatSessionController) {
    this.controller = chatSessionController;
  }
}
