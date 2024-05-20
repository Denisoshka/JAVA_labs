package client.model.chat_modules.submodules;

import client.facade.ChatSessionController;
import client.model.chat_modules.interfaces.ChatModule;
import client.model.main_context.ChatSessionExecutor;
import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.subtypes.FileDTO;
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
  final static String FILE_REQUEST_ENCODING = "base64";

  private final BlockingQueue<Document> moduleExchanger;
  private final ExecutorService executor;
  private final ChatSessionExecutor sessionExecutor;
  private final ChatSessionController sessionController;
  private final FileDTO.FileUploadDTOConverter uploadDTOConverter;
  private final FileDTO.FileDownloadDTOConverter downloadDTOConverter;
  private final Logger moduleLogger;
  private final FileManager fileManager;

  public FileModule(ChatSessionExecutor chatSessionExecutor) throws IOException {
    this.fileManager = new SimpleFileManager("downloaded_files");
    this.sessionExecutor = chatSessionExecutor;
    this.sessionController = sessionExecutor.getChatSessionController();
    this.moduleExchanger = sessionExecutor.getModuleExchanger();
    this.executor = sessionExecutor.getChatModuleExecutor();
    FileDTO.FileDTOConverter converter = (FileDTO.FileDTOConverter) sessionExecutor.getDTOConverterManager().getConverterBySection(RequestDTO.DTO_SECTION.FILE);
    this.uploadDTOConverter = converter.getFileUploadDTOConverter();
    this.downloadDTOConverter = converter.getFileDownloadDTOConverter();
    this.moduleLogger = sessionExecutor.getModuleLogger();
  }


  @Override
  public void commandAction(RequestDTO.BaseCommand command, Object additionalArg) {
    executor.execute(() -> {
      responseActon(command);
      if (command.getCommandType() == RequestDTO.COMMAND_TYPE.UPLOAD) {
        uploadAction((FileDTO.UploadCommand) command, (Path) additionalArg);
      } else if (command.getCommandType() == RequestDTO.COMMAND_TYPE.DOWNLOAD) {
        downloadAction((FileDTO.DownloadCommand) command);
      }
    });
  }

  @Override
  public void responseActon(RequestDTO.BaseCommand command) {
    executor.execute(() -> {
      if (command.getCommandType() == RequestDTO.COMMAND_TYPE.UPLOAD) {
        uploadResponse((FileDTO.UploadCommand) command);
      } else if (command.getCommandType() == RequestDTO.COMMAND_TYPE.DOWNLOAD) {
        downloadResponse((FileDTO.DownloadCommand) command);
      }
    });
  }

  @Override
  public void eventAction(RequestDTO.BaseEvent event) {
    FileDTO.Event fileEvent = (FileDTO.Event) event;
    sessionController.onFileUploadEvent(new FileDTO.Event(
            fileEvent.getId(), fileEvent.getFrom(),
            fileEvent.getName(), fileEvent.getSize(),
            fileEvent.getMimeType()
    ));
  }

  private void uploadResponse(FileDTO.UploadCommand command) {
    try {
      RequestDTO.BaseResponse response = (RequestDTO.BaseResponse) uploadDTOConverter.deserialize(moduleExchanger.take());
      if (response.getResponseType() == RequestDTO.RESPONSE_TYPE.SUCCESS) {
        FileDTO.UploadSuccess responseSuccess = (FileDTO.UploadSuccess) response;
        sessionController.onFileUploadResponse(new FileDTO.Event(
                responseSuccess.getId(), null,
                command.getName(), command.getContent().length,
                command.getMimeType()
        ));
      } else if (response.getResponseType() == RequestDTO.RESPONSE_TYPE.ERROR) {
        moduleLogger.info(STR."Upload failed\{((FileDTO.Error) response).getMessage()}");
      } else {
        moduleLogger.info(STR."Unknown response type\{response.getResponseType()}");
      }
    } catch (UnableToDeserialize e) {
      moduleLogger.error(e.getMessage(), e);
    } catch (InterruptedException _) {
    }
  }

  private void downloadResponse(FileDTO.DownloadCommand command) {
    try {
      RequestDTO.BaseResponse response = (RequestDTO.BaseResponse) downloadDTOConverter.deserialize(moduleExchanger.take());
      if (response.getResponseType() == RequestDTO.RESPONSE_TYPE.SUCCESS) {
        FileDTO.DownloadSuccess responseSuccess = (FileDTO.DownloadSuccess) response;
        try {
          fileManager.saveFileEntry(new FileDTO.UploadCommand(
                  responseSuccess.getName(), responseSuccess.getMimeType(),
                  responseSuccess.getEncoding(), responseSuccess.getContent())
          );
        } catch (IOException e) {
          moduleLogger.error(e.getMessage(), e);
        }
      } else if (response.getResponseType() == RequestDTO.RESPONSE_TYPE.ERROR) {
        moduleLogger.error("Download failed");
      } else {
        moduleLogger.error("Unknown response type");
      }
    } catch (UnableToDeserialize e) {
      moduleLogger.error(e.getMessage(), e);
    } catch (InterruptedException _) {
    }
  }

  private void uploadAction(FileDTO.UploadCommand command, Path path) {
    IOProcessor ioProcessor = sessionExecutor.getIOProcessor();
    try {
      byte[] content = Base64.getEncoder().encode(Files.readAllBytes(path));
      String name = path.getFileName().toString();
      String mimeType = Files.probeContentType(path);
      try {
        ioProcessor.sendMessage(uploadDTOConverter.serialize(
                new FileDTO.UploadCommand(name, mimeType, FILE_REQUEST_ENCODING, content)).getBytes()
        );
      } catch (IOException e) {
        try {
          sessionExecutor.shutdownConnection();
        } catch (IOException e1) {
          moduleLogger.error(e1.getMessage(), e1);
        }
      }
    } catch (UnableToSerialize e) {
      moduleLogger.error(e.getMessage(), e);
    } catch (IOException e) {

      moduleLogger.error(e.getMessage(), e);
    }
  }

  private void downloadAction(FileDTO.DownloadCommand command) {
    IOProcessor ioProcessor = sessionExecutor.getIOProcessor();
    try {
      ioProcessor.sendMessage(downloadDTOConverter.serialize(command).getBytes());
    } catch (UnableToSerialize e) {
      moduleLogger.error(e.getMessage(), e);
    } catch (IOException e) {
      try {
        sessionExecutor.shutdownConnection();
      } catch (IOException e1) {
        moduleLogger.error(e1.getMessage(), e1);
      }
      moduleLogger.error(e.getMessage(), e);
    }
  }
}
