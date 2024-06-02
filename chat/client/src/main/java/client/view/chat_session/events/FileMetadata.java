package client.view.chat_session.events;

public class FileMetadata {
  private final String fileId;
  private final String fileName;
  private final long size;
  private final String mimeType;

  public FileMetadata(String fileId, String fileName, long size, String mimeType) {
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

  public long getSize() {
    return size;
  }

  public String getMimeType() {
    return mimeType;
  }
}