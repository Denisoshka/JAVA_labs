package client.view.chat_session.events;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FileMetadata {
  private final String fileId;
  private final String fileName;
  private final int size;
  private final String mimeType;

  public FileMetadata(String fileId, String fileName, int size, String mimeType) {
    this.fileId = fileId;
    this.fileName = fileName;
    this.mimeType = mimeType;
    this.size = size;
  }

  public String getFileId() {
    return fileId;
  }

  public String getFileName() {
    return fileName;
  }

  public int getSize() {
    return size;
  }

  public String getMimeType() {
    return mimeType;
  }
}