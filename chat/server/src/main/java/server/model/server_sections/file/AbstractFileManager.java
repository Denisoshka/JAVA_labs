package server.model.server_sections.file;

import dto.subtypes.FileDTO;

import java.nio.file.Path;

public interface AbstractFileManager {
  record SaveRet(String id, String exmessage) {
  }

  record FileEntry(String name, String mimeType, String encoding, int size, Path path) {
  }

  FileEntry getFileEntry(String ID);

  SaveRet saveFileEntry(FileDTO.UploadCommand entry);
}
