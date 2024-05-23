package client.view.chat_session;

import client.facade.ChatSessionController;
import client.view.ControllerIntroduce;
import client.view.chat_session.events.FileChoseContext;
import client.view.chat_session.events.FileMetadata;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;

import java.io.IOException;

public class FileChooseWindow extends VBox implements FileChoseContext, ControllerIntroduce {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(FileChooseWindow.class);
  @FXML
  private ListView<FileMetadata> filePreviews;
  private ChatSessionController controller;

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
          setGraphic(new FilePreviewCell(item));
        }
      }
    });
  }

  @Override
  public void onFileDownloadChoose(String fileId) {
    log.info(STR."choose file \{fileId} ");
    this.controller.downloadFile(fileId);
  }

  @Override
  public void onFileUpload(FileMetadata fileMetadata) {
    Platform.runLater(() -> filePreviews.getItems().add(fileMetadata));
  }

  @Override
  public void setChatSessionController(ChatSessionController chatSessionController) {
    this.controller = chatSessionController;
  }

  private class FilePreviewCell extends VBox {
    private final String fileId;

    FilePreviewCell(FileMetadata fileMetadata) {
      Label fileIdLabel = new Label(fileMetadata.getFileId());
      Label fileNameLabel = new Label(fileMetadata.getFileName());
      Label fileSizeLabel = new Label(String.valueOf(fileMetadata.getSize()));
      Label fileMimeTypeLabel = new Label(fileMetadata.getMimeType());
      Button downloadButton = new Button("Download");
      var tHBox = new HBox(fileIdLabel, fileNameLabel);
      var bHBox = new HBox(fileSizeLabel, fileMimeTypeLabel);
      super(tHBox, bHBox, downloadButton);
      super.setSpacing(10);
      tHBox.setSpacing(10);
      bHBox.setSpacing(10);
      fileIdLabel.setMaxWidth(10);
      this.fileId = fileMetadata.getFileId();
      downloadButton.setOnAction(this::fileChosen);
    }

    private void fileChosen(Event event) {
      FileChooseWindow.this.onFileDownloadChoose(fileId);
    }
  }
}
