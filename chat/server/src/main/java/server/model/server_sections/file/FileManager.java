package server.model.server_sections.file;

import dto.subtypes.FileDTO;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FileManager implements AbstractFileManager {
  //  todo delete hardcode
  private final static String SUPPORTED_ENCODING = "base64";
  private final String fileSectionPath;
  private final ConcurrentHashMap<String, FileEntry> existingFiles = new ConcurrentHashMap<>();
  private final AtomicInteger idCounter = new AtomicInteger(0);


  public FileManager(String filesDir) throws IOException {
    Path path = Paths.get(filesDir);
    Files.createDirectories(path);
    fileSectionPath = filesDir;
  }

  /**
   * @return {@code FileEntry} if it by {@code ID}  exists else {@code null}
   */
  public FileEntry getFileEntry(String ID) {
    return existingFiles.get(ID);
  }

  /**
   * save file with specified {@code FileEntry} with {@code content}
   *
   * @return {@code String id} on success
   */
  public SaveRet saveFileEntry(FileDTO.UploadCommand entry) {
    if (!entry.getEncoding().equals(SUPPORTED_ENCODING)) {
      return new SaveRet(null, STR."support only \{SUPPORTED_ENCODING} encoding");
    }

    FileEntry tmp;
    String fileID = null;
    if (existingFiles.get(entry.getName()) != null) {
      fileID = STR."\{entry.getName()}_\{idCounter.getAndIncrement()}";
    } else fileID = entry.getName();

    Path filePath = Paths.get(fileSectionPath, fileID);

    try (BufferedOutputStream writer = new BufferedOutputStream(Files.newOutputStream(filePath))) {
      writer.write(entry.getContent());
      existingFiles.put(
              fileID,
              new FileEntry(
                      entry.getName(), entry.getMimeType(), entry.getEncoding(),
                      entry.getContent().length, filePath
              )
      );
    } catch (IOException | NullPointerException e) {
      return new SaveRet(null, e.getMessage());
    }
    return new SaveRet(fileID, null);
  }
}
