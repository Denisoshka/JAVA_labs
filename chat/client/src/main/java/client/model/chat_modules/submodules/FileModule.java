package client.model.chat_modules.submodules;

import client.facade.ChatSessionController;
import client.model.chat_modules.interfaces.ChatModule;
import client.model.main_context.ChatSessionExecutor;
import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.interfaces.DTOConverterManagerInterface;
import dto.interfaces.DTOInterfaces;
import dto.subtypes.FileDTO;
import dto.subtypes.file.*;
import file_section.FileManager;
import file_section.SimpleFileManager;
import io_processing.IOProcessor;
import org.slf4j.Logger;
import org.w3c.dom.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class FileModule implements ChatModule {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(FileModule.class);
  final static String FILE_REQUEST_ENCODING = "base64";

  private final BlockingQueue<Document> moduleExchanger;
  private final ExecutorService executor;
  private final ChatSessionExecutor sessionExecutor;
  private final ChatSessionController sessionController;
  private final FileUploadDTOConverter uploadDTOConverter;
  private final FileDownloadDTOConverter downloadDTOConverter;
  private final FileListFileDTOConverter listFileDTOConverter;

  private final FileManager fileManager;

  public FileModule(ChatSessionExecutor chatSessionExecutor) throws IOException {
    this.fileManager = new SimpleFileManager("downloaded_files");
    this.sessionExecutor = chatSessionExecutor;
    this.sessionController = sessionExecutor.getChatSessionController();
    this.moduleExchanger = sessionExecutor.getModuleExchanger();
    this.executor = sessionExecutor.getChatModuleExecutor();
    FileDTOConverter converter = (FileDTOConverter) sessionExecutor.getDTOConverterManager().getConverterBySection(RequestDTO.DTO_SECTION.FILE);
    this.uploadDTOConverter = converter.getFileUploadDTOConverter();
    this.downloadDTOConverter = converter.getFileDownloadDTOConverter();
    this.listFileDTOConverter = converter.getListFileDTOConverter();
  }

  @Override
  public void eventAction(Document root) {
    try {
      var eventType = DTOConverterManagerInterface.getDTOEvent(root);
      if (eventType == RequestDTO.EVENT_TYPE.FILE) {
        FileEvent fileEvent = (FileEvent) uploadDTOConverter.deserialize(root);
        sessionController.onFileUploadEvent(new FileEvent(
                fileEvent.getId(), fileEvent.getFrom(),
                fileEvent.getName(), fileEvent.getSize(),
                fileEvent.getMimeType()
        ));
      } else {
        log.debug("unsupported event type: {}", DTOConverterManagerInterface.getSTRDTOEvent(root));
      }
    } catch (UnableToDeserialize e) {
      log.error(e.getMessage(), e);
    }
  }

  public void uploadResponse(UploadCommand command) {
    executor.execute(() -> {
      try {
        DTOInterfaces.RESPONSE_DTO response = (DTOInterfaces.RESPONSE_DTO) uploadDTOConverter.deserialize(moduleExchanger.take());
        if (response.getResponseType() == RequestDTO.RESPONSE_TYPE.SUCCESS) {
          UploadSuccess responseSuccess = (UploadSuccess) response;
          /*sessionController.onFileUploadResponse(new FileDTO.Event(
                  responseSuccess.getId(), null,
                  command.getName(), command.getContent().length,
                  command.getMimeType()
          ));*/
        } else if (response.getResponseType() == RequestDTO.RESPONSE_TYPE.ERROR) {
          log.info(STR."Upload failed \{((FileError) response).getMessage()}");
        } else {
          log.info(STR."Unknown response type \{response.getResponseType()}");
        }
      } catch (UnableToDeserialize e) {
        log.error(e.getMessage(), e);
      } catch (InterruptedException _) {
      }
    });
  }

  public void downloadResponse(DownloadCommand command) {
    executor.execute(() -> {
      try {
        DTOInterfaces.RESPONSE_DTO response = (DTOInterfaces.RESPONSE_DTO) downloadDTOConverter.deserialize(moduleExchanger.take());
        log.info(STR."download response: \{response.getResponseType()}");
        if (response.getResponseType() == RequestDTO.RESPONSE_TYPE.SUCCESS) {
          FileDTO.DownloadSuccess responseSuccess = (FileDTO.DownloadSuccess) response;
          log.info(STR."successfully downloaded file id: \{responseSuccess.getId()}, name: \{responseSuccess.getName()}, " +
                  STR."mimeType: \{responseSuccess.getMimeType()}, encoding \{responseSuccess.getEncoding()}");
          if (!responseSuccess.getEncoding().equals(FILE_REQUEST_ENCODING)) {
            log.info(STR."not supported encoding\{responseSuccess.getEncoding()}");
            return;
          }
          try {
            fileManager.saveFileEntry(
                    responseSuccess.getName(), responseSuccess.getMimeType(),
                    "", Base64.getDecoder().decode(responseSuccess.getContent())
            );
          } catch (IOException e) {
            log.error(e.getMessage(), e);
          }
        } else if (response.getResponseType() == RequestDTO.RESPONSE_TYPE.ERROR) {
          log.error("download failed");
        } else {
          log.error("unknown response type");
        }
      } catch (UnableToDeserialize e) {
        log.error(e.getMessage(), e);
      } catch (InterruptedException _) {
      }
    });
  }

  public void uploadAction(Path path) {
    executor.execute(() -> {
      try {
        IOProcessor ioProcessor = sessionExecutor.getIOProcessor();
        byte[] content = Base64.getEncoder().encode(Files.readAllBytes(path));
        String name = path.getFileName().toString();
        String mimeType = Files.probeContentType(path);
        try {
          final FileDTO.UploadCommand uploadCommand = new FileDTO.UploadCommand(name, mimeType, FILE_REQUEST_ENCODING, content);
          log.info(STR."send file name: \{name}, mimeType: \{mimeType}, size: \{content.length}");
          ioProcessor.sendMessage(uploadDTOConverter.serialize(uploadCommand).getBytes());
          uploadResponse(uploadCommand);

        } catch (IOException e) {
          try {
            sessionExecutor.shutdownConnection();
          } catch (IOException e1) {
            log.error(e1.getMessage(), e1);
          }
        }
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
    });
  }

  public void downloadAction(FileDTO.DownloadCommand command) {
    executor.execute(() -> {
      log.info(STR."processing download request \{command.getId()}");
      IOProcessor ioProcessor = sessionExecutor.getIOProcessor();
      try {
        ioProcessor.sendMessage(downloadDTOConverter.serialize(command).getBytes());
        downloadResponse(command);
      } catch (UnableToSerialize e) {
        log.error(e.getMessage(), e);
      } catch (IOException e) {
        try {
          sessionExecutor.shutdownConnection();
        } catch (IOException e1) {
          log.error(e1.getMessage(), e1);
        }
        log.error(e.getMessage(), e);
      }
    });
  }


  public void fileListAction() {
    executor.execute(() -> {
      log.info("processing file list");
      try {
        IOProcessor ioProcessor = sessionExecutor.getIOProcessor();
        ioProcessor.sendMessage(listFileDTOConverter.serialize(new FileDTO.ListFileCommand()).getBytes());
        fileListResponse();
      } catch (UnableToSerialize e) {
        log.error(e.getMessage(), e);
      } catch (IOException e) {
        log.error(e.getMessage(), e);
        try {
          sessionExecutor.shutdownConnection();
        } catch (IOException _) {
        }
      }
    });
  }

  public void fileListResponse() {
    executor.execute(() -> {
      try {
        DTOInterfaces.RESPONSE_DTO response = (DTOInterfaces.RESPONSE_DTO) listFileDTOConverter.deserialize(moduleExchanger.take());
        if (response.getResponseType() == RequestDTO.RESPONSE_TYPE.SUCCESS) {
          FileDTO.ListFileSuccess responseSuccess = (FileDTO.ListFileSuccess) response;
          log.debug(responseSuccess.getFiles().toString());
          sessionController.onListFileResponse(responseSuccess.getFiles());
        } else {
          FileDTO.Error responseError = (FileDTO.Error) response;
          log.error(responseError.getMessage());
        }
      } catch (UnableToDeserialize e) {
        log.warn(e.getMessage(), e);
      } catch (InterruptedException _) {
      }
    });
  }
}
