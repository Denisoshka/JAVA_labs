package server.model.server_sections.file;

import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.interfaces.DTOConverterManagerInterface;
import dto.subtypes.FileDTO;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import server.model.Server;
import server.model.io_processing.ServerConnection;
import server.model.server_sections.interfaces.AbstractSection;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;

public class FileSection implements AbstractSection {
  private final FileDTO.FileUploadDTOConverter uploadDTOConverter;
  private final FileDTO.FileDownloadDTOConverter downloadDTOConverter;
  private final Logger moduleLogger;
  private final Server server;
  private final FileManager fileManager;

  public FileSection(Server server) throws IOException {
    FileDTO.FileDTOConverter mainConverter = (FileDTO.FileDTOConverter) server.getConverterManager().getConverter(RequestDTO.DTO_SECTION.FILE);
    this.server = server;
    this.moduleLogger = server.getModuleLogger();
    this.uploadDTOConverter = mainConverter.getFileUploadDTOConverter();
    this.downloadDTOConverter = mainConverter.getFileDownloadDTOConverter();
    this.fileManager = new FileManager(".saved_files");
  }


  @Override
  public void perform(ServerConnection connection, Document root, RequestDTO.DTO_TYPE type, RequestDTO.DTO_SECTION section) {
    if (type != RequestDTO.DTO_TYPE.COMMAND || section != RequestDTO.DTO_SECTION.FILE) {
      onUnsupportedType(type, section, connection);
      return;
    }
    RequestDTO.COMMAND_TYPE commandType = DTOConverterManagerInterface.getDTOCommand(root);
    if (commandType == RequestDTO.COMMAND_TYPE.UPLOAD) {
      onUploadRequest(root, connection);
    } else if (commandType == RequestDTO.COMMAND_TYPE.DOWNLOAD) {
      onDownloadRequest(root, connection);
    } else {
//      todo implements this
      onUnsupportedType(type, section, connection);
    }
  }

  private void onUploadRequest(Document root, ServerConnection connection) {
    try {
      FileDTO.UploadCommand command = (FileDTO.UploadCommand) uploadDTOConverter.deserialize(root);
      AbstractFileManager.SaveRet ret = fileManager.saveFileEntry(command);
      if (ret.exmessage() != null) {
        connection.sendMessage(uploadDTOConverter.serialize(new FileDTO.Error(ret.exmessage())).getBytes());
      } else {
        connection.sendMessage(uploadDTOConverter.serialize(new FileDTO.UploadSuccess(ret.id())).getBytes());
      }
    } catch (UnableToDeserialize e) {
      try {
        connection.sendMessage(uploadDTOConverter.serialize(new FileDTO.Error(e.getMessage())).getBytes());
      } catch (UnableToDeserialize _) {

      } catch (IOException e1) {
        moduleLogger.error(e1.getMessage(), e1);
        server.submitExpiredConnection(connection);
      }
    } catch (IOException e) {
      moduleLogger.error(e.getMessage(), e);
      server.submitExpiredConnection(connection);
    }
  }

  private void onDownloadRequest(Document root, ServerConnection connection) {
    try {
      FileDTO.DownloadCommand command = (FileDTO.DownloadCommand) downloadDTOConverter.deserialize(root);
      AbstractFileManager.FileEntry entry = fileManager.getFileEntry(command.getId());
      if (entry == null) {
        connection.sendMessage(downloadDTOConverter.serialize(
                new FileDTO.Error(STR."file with ID: \{command.getId()} not avalibe")
        ).getBytes());
      } else {
        byte[] data = null;
        try (BufferedInputStream reader = new BufferedInputStream(Files.newInputStream(entry.path()))) {
          data = reader.readNBytes(entry.size());
        } catch (IOException e) {
          moduleLogger.error(e.getMessage(), e);
        }
        if (data != null) {
          connection.sendMessage(downloadDTOConverter.serialize(
                  new FileDTO.DownloadSuccess(command.getId(), entry.name(), entry.mimeType(), entry.encoding(), data)
          ).getBytes());
        } else {
          connection.sendMessage(downloadDTOConverter.serialize(
                  new FileDTO.Error(STR."file with ID: \{command.getId()} not avalibe")
          ).getBytes());
        }
      }
    } catch (UnableToDeserialize e) {
      try {
        connection.sendMessage(
                downloadDTOConverter.serialize(new FileDTO.Error(STR."unable to serialize response")).getBytes()
        );
      } catch (UnableToSerialize e1) {
        moduleLogger.trace(e1.getMessage(), e1);
      } catch (IOException e1) {
        moduleLogger.trace(e1.getMessage(), e1);
        server.submitExpiredConnection(connection);
      }
    } catch (IOException e) {
      moduleLogger.trace(e.getMessage(), e);
      server.submitExpiredConnection(connection);
    }
  }

  private void onUnsupportedType(RequestDTO.DTO_TYPE type, RequestDTO.DTO_SECTION section, ServerConnection connection) {
    var errmsg = STR."not support type: \{type.name()} or section \{section}";
    moduleLogger.info(errmsg);
    try {
      connection.sendMessage(uploadDTOConverter.serialize(new FileDTO.Error(errmsg)).getBytes());
    } catch (IOException e) {
      server.submitExpiredConnection(connection);
    }
  }


}
