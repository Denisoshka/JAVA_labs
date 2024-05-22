package client.view.chat_session;

import client.facade.ChatSessionController;
import client.view.chat_session.events.FileChoseContext;
import client.view.chat_session.events.FilePreview;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class FileChooseWindow extends VBox implements FileChoseContext {
  @FXML
  private ListView<FilePreview> filePreviews;
  private ChatSessionController controller;

  public FileChooseWindow(ChatSessionController chatSessionController) {
    FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("FileChoseWindow.fxml"));
    fxmlloader.setRoot(this);
    fxmlloader.setController(this);
    try {
      fxmlloader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
    this.controller = chatSessionController;
    initialize();
  }

  @FXML
  private void initialize() {
    ObservableList<FilePreview> previews = FXCollections.observableArrayList();
    filePreviews.setItems(previews);
    filePreviews.setCellFactory(_ -> new ListCell<>() {
      @Override
      protected void updateItem(FilePreview item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setGraphic(null);
        } else {
          setGraphic(item);
        }
      }
    });
  }

  @Override
  public void onFileDownloadChoose(FilePreview fileMetadata) {
    controller.downloadFile(fileMetadata.getFileId());
  }

  @Override
  public void onFileUpload(FilePreview fileMetadata) {
    filePreviews.getItems().add(fileMetadata);
  }
}
