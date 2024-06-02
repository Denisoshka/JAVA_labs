package client.view.chat_session.events;

import client.facade.ChatSessionController;
import client.view.chat_session.FileChooseWindow;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FilePreviewCell extends VBox {
  private final String fileId;
  private final ChatSessionController controller;

  public FilePreviewCell(ChatSessionController controller, FileMetadata fileMetadata) {
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
    downloadButton.setOnAction(this::fileChosen);
    this.fileId = fileMetadata.getFileId();
    this.controller = controller;
  }

  private void fileChosen(Event event) {
    controller.downloadFile(Long.valueOf(fileId));
  }
}

