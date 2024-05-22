package server.model.server_sections.file;

import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.interfaces.DTOConverterManagerInterface;
import dto.subtypes.FileDTO;
import file_section.FileManager;
import file_section.SimpleFileManager;
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
  private final SimpleFileManager simpleFileManager;

  public FileSection(Server server) throws IOException {
    FileDTO.FileDTOConverter mainConverter = (FileDTO.FileDTOConverter) server.getConverterManager().getConverterBySection(RequestDTO.DTO_SECTION.FILE);
    this.server = server;
    this.moduleLogger = server.getModuleLogger();
    this.uploadDTOConverter = mainConverter.getFileUploadDTOConverter();
    this.downloadDTOConverter = mainConverter.getFileDownloadDTOConverter();
    this.simpleFileManager = new SimpleFileManager(".saved_files");
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
    FileDTO.UploadCommand command = null;
    String id = null;
    try {
      command = (FileDTO.UploadCommand) uploadDTOConverter.deserialize(root);
    } catch (UnableToDeserialize e) {
      try {
        connection.sendMessage(uploadDTOConverter.serialize(new FileDTO.Error(e.getMessage())).getBytes());
      } catch (IOException ioe) {
        moduleLogger.error(ioe.getMessage(), ioe);
      }
      moduleLogger.error(e.getMessage(), e);
      return;
    }
    try {
      id = simpleFileManager.saveFileEntry(command.getName(), command.getMimeType(), command.getEncoding(), command.getContent());
    } catch (IOException e) {
      try {
        connection.sendMessage(uploadDTOConverter.serialize(new FileDTO.Error(e.getMessage())).getBytes());
      } catch (IOException ioe) {
        moduleLogger.error(ioe.getMessage(), ioe);
      }
      moduleLogger.error(e.getMessage(), e);
      return;
    }
    try {
      byte[] serEvent = uploadDTOConverter.serialize(new FileDTO.Event(
              id, connection.getConnectionName(), command.getName(),
              command.getContent().length, command.getMimeType()
      )).getBytes();

      for (var conn : server.getConnections()) {
        try {
          if (!conn.isExpired()) conn.sendMessage(serEvent);
        } catch (IOException e) {
          server.submitExpiredConnection(connection);
        }
      }
      connection.sendMessage(uploadDTOConverter.serialize(new FileDTO.UploadSuccess(id)).getBytes());
    } catch (UnableToSerialize e) {
      try {
        connection.sendMessage(uploadDTOConverter.serialize(new FileDTO.Error(e.getMessage())).getBytes());
      } catch (IOException ioe) {
        moduleLogger.error(ioe.getMessage(), ioe);
      }
      moduleLogger.error(e.getMessage(), e);
    } catch (IOException e) {
      moduleLogger.error(e.getMessage(), e);
      server.submitExpiredConnection(connection);
    }
  }

  private void onDownloadRequest(Document root, ServerConnection connection) {
    try {
      FileDTO.DownloadCommand command = (FileDTO.DownloadCommand) downloadDTOConverter.deserialize(root);
      FileManager.StorageFileEntry entry = simpleFileManager.getFileEntry(command.getId());
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
