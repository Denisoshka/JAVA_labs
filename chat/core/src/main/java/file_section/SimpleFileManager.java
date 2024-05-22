package file_section;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleFileManager implements FileManager {
  //  todo delete hardcode
  private final static String SUPPORTED_ENCODING = "base64";
  private final String fileSectionPath;
  private final ConcurrentHashMap<String, StorageFileEntry> existingFiles = new ConcurrentHashMap<>();
  private final AtomicInteger idCounter = new AtomicInteger(0);

  public SimpleFileManager(String filesDir) throws IOException {
    Path path = Paths.get(filesDir);
    Files.createDirectories(path);
    fileSectionPath = filesDir;
  }

  /**
   * @return {@code FileEntry} if it by {@code ID}  exists else {@code null}
   */
  public StorageFileEntry getFileEntry(String ID) {
    return existingFiles.get(ID);
  }

  /**
   * save file with specified {@code FileEntry} with {@code content}
   * todo remake
   *
   * @return {@code String id} on success
   */
  public String saveFileEntry(String fileName, String mimeType,
                              String encoding, byte[] content) throws IOException {
    StorageFileEntry tmp;
    String fileID = null;
    if (existingFiles.get(fileName) != null) {
      fileID = STR."\{fileName}_\{idCounter.getAndIncrement()}";
    } else fileID = fileName;
    Path filePath = Paths.get(fileSectionPath, fileID);

    try (BufferedOutputStream writer = new BufferedOutputStream(Files.newOutputStream(filePath))) {
      writer.write(content);
      existingFiles.put(fileID, new StorageFileEntry(fileName, mimeType, encoding, content.length, filePath));
    }
    return fileID;
  }
}
