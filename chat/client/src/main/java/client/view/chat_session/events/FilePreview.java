package client.view.chat_session.events;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FilePreview extends VBox implements FileMetadata {
  private String fileId;
  private Button downloadButton;
  private FileChoseContext fileChoseContext;

  public FilePreview(FileChoseContext fileChoseContext, String fileId, String fileName, int Size, String mimeType) {
    Label fileIdLabel = new Label(fileId);
    Label fileNameLabel = new Label(fileName);
    Label fileSizeLabel = new Label(String.valueOf(Size));
    Label fileMimeTypeLabel = new Label(mimeType);
    Button downloadButton = new Button("download");

    super(fileNameLabel, new HBox(fileSizeLabel, fileMimeTypeLabel, fileIdLabel), downloadButton);

    this.fileChoseContext = fileChoseContext;
    this.fileId = fileId;
    this.setSpacing(10);
    fileIdLabel.setMaxWidth(10);
    initialize();
  }

  @FXML
  public void initialize() {
    downloadButton.setOnAction(this::fileChosen);
  }

  @FXML
  private void fileChosen(Event event) {
    fileChoseContext.onFileDownloadChoose(this);
  }

  @Override
  public String getFileId() {
    return fileId;
  }
}