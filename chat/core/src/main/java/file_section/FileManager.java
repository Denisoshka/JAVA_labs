package file_section;

import dto.subtypes.FileDTO;

import java.io.IOException;
import java.nio.file.Path;

public interface FileManager {
  record SaveRet(String id, String exmessage) {
  }

  record StorageFileEntry(String name, String mimeType, String encoding, int size, Path path) {
  }

  StorageFileEntry getFileEntry(String ID);

  String saveFileEntry(FileDTO.UploadCommand entry) throws IOException;
}
