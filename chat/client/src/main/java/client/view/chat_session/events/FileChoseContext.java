package client.view.chat_session.events;


public interface FileChoseContext {
  void onFileDownloadChoose(String fileId);

  void onFileUpload(FileMetadata fileMetadata);
}
