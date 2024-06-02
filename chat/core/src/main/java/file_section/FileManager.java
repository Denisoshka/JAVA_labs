package file_section;

import java.io.IOException;
import java.nio.file.Path;

public interface FileManager {
  record SaveRet(String id, String exmessage) {
  }

  record StorageFileEntry(String name, String mimeType, String encoding, int size, Path path) {
  }

  StorageFileEntry getFileEntry(String ID);

  String saveFileEntry(String fileName, String mimeType, String encoding, byte[] content) throws IOException;
}
