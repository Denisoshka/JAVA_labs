package client.view.chat_session.events;


public interface FileChoseContext {
  void onFileDownloadChoose(FilePreview fileMetadata);

  void onFileUpload(FilePreview fileMetadata);
}
