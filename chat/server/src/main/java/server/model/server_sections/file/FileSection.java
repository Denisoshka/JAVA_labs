package server.model.server_sections.file;

import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.interfaces.DTOConverterManagerInterface;
import dto.subtypes.FileDTO;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import server.model.Server;
import server.model.connection_section.ServerConnection;
import server.model.server_sections.interfaces.AbstractSection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileSection implements AbstractSection {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(FileSection.class);
  private static final String FILE_ENCODING = "base64";

  private final FileDTO.FileUploadDTOConverter uploadDTOConverter;
  private final FileDTO.FileDownloadDTOConverter downloadDTOConverter;
  private final FileDTO.FileListFileDTOConverter listFileDTOConverter;
  private final Logger moduleLogger;
  private final Server server;
  private final SmallFileDAO smallFileDAO = new SmallFileDAO();

  public FileSection(Server server) throws IOException {
    FileDTO.FileDTOConverter mainConverter = (FileDTO.FileDTOConverter) server.getConverterManager().getConverterBySection(RequestDTO.DTO_SECTION.FILE);
    this.server = server;
    this.moduleLogger = server.getModuleLogger();
    this.uploadDTOConverter = mainConverter.getFileUploadDTOConverter();
    this.downloadDTOConverter = mainConverter.getFileDownloadDTOConverter();
    this.listFileDTOConverter = mainConverter.getListFileDTOConverter();
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
    } else if (commandType == RequestDTO.COMMAND_TYPE.LISTFILE) {
      onListFileRequest(root, connection);
    } else {
//      todo implements this
      onUnsupportedType(type, section, connection);
    }
  }

  private void onUploadRequest(Document root, ServerConnection connection) {
    moduleLogger.info(STR."Upload request from \{connection.getConnectionName()}");
    FileDTO.UploadCommand command = null;
    Long id = null;
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
      if (!command.getEncoding().equals(FILE_ENCODING)) {
        connection.sendMessage(uploadDTOConverter.serialize(
                new FileDTO.Error(STR."support only \{FILE_ENCODING} encoding")
        ).getBytes());
        return;
      }

      id = (smallFileDAO.saveSmallFile(command.getName(), connection.getConnectionName(),
              command.getMimeType(), command.getContent().length, command.getContent()
      ));
      if (id != null) {
        var event = new FileDTO.Event(id, connection.getConnectionName(),
                command.getName(), command.getContent().length, command.getMimeType());
        var strEvent = uploadDTOConverter.serialize(event);
        byte[] serEvent = strEvent.getBytes();
        for (var conn : server.getConnections()) {
          try {
            if (!conn.isExpired()) conn.sendMessage(serEvent);
          } catch (IOException e) {
            server.submitExpiredConnection(connection);
          }
        }
        connection.sendMessage(uploadDTOConverter.serialize(new FileDTO.UploadSuccess(id)).getBytes());
      } else {
        connection.sendMessage(uploadDTOConverter.serialize(new FileDTO.Error("unable to save file")).getBytes());
      }
    } catch (UnableToSerialize e) {
      try {
        connection.sendMessage(uploadDTOConverter.serialize(new FileDTO.Error(e.getMessage())).getBytes());
      } catch (IOException ioe) {
        moduleLogger.error(ioe.getMessage(), ioe);
        server.submitExpiredConnection(connection);
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
      moduleLogger.info(STR."Download request \{command.getId()} from \{connection.getConnectionName()}");

      SmallFileEntity fileEntity = smallFileDAO.getFile(command.getId());
      if (fileEntity != null) {
        connection.sendMessage(downloadDTOConverter.serialize(
                new FileDTO.DownloadSuccess(command.getId(), fileEntity.getFileName(), fileEntity.getMimeType(), FILE_ENCODING, fileEntity.getContent())
        ).getBytes());
      } else {
        connection.sendMessage(downloadDTOConverter.serialize(
                new FileDTO.Error(STR."file with ID: \{command.getId()} not avalibe")
        ).getBytes());
      }
    } catch (UnableToDeserialize e) {
      try {
        connection.sendMessage(
                downloadDTOConverter.serialize(new FileDTO.Error(e.getMessage())).getBytes()
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

  public void onListFileRequest(Document root, ServerConnection connection) {
    try {
      FileDTO.ListFileCommand command = (FileDTO.ListFileCommand) listFileDTOConverter.deserialize(root);
      List<SmallFileEntity> files = smallFileDAO.getFilesPreview();
      if (files != null) {
        List<FileDTO.FileEntity> rez = new ArrayList<>(files.size());
        for (var file : files) {
          rez.add(new FileDTO.FileEntity(file.getId(), file.getUserName(), file.getFileName(), file.getSize(), file.getMimeType()));
        }
        connection.sendMessage(listFileDTOConverter.serialize(new FileDTO.ListFileSuccess(rez)).getBytes());
      } else {
        connection.sendMessage(listFileDTOConverter.serialize(new FileDTO.Error()).getBytes());
      }
    } catch (UnableToDeserialize | UnableToSerialize e) {
      try {
        connection.sendMessage(listFileDTOConverter.serialize(new FileDTO.Error(e.getMessage())).getBytes());
      } catch (UnableToSerialize e1) {
        log.warn(e.getMessage(), e);
      } catch (IOException e1) {
        server.submitExpiredConnection(connection);
      }
    } catch (IOException e) {
      server.submitExpiredConnection(connection);
    }
  }

  private void onUnsupportedType(RequestDTO.DTO_TYPE type, RequestDTO.DTO_SECTION section, ServerConnection connection) {
    var errmsg = STR."not support type: \{type.name()} or section \{section}";
//    todo make get of type by str
    moduleLogger.info(errmsg);
    try {
      connection.sendMessage(uploadDTOConverter.serialize(new FileDTO.Error(errmsg)).getBytes());
    } catch (IOException e) {
      moduleLogger.error(e.getMessage(), e);
      server.submitExpiredConnection(connection);
    }
  }
}
