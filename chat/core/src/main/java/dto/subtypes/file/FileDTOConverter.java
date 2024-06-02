package dto.subtypes.file;

import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.interfaces.DTOConverter;
import dto.interfaces.DTOConverterManagerInterface;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.JAXBException;
import org.w3c.dom.Document;

import static dto.RequestDTO.COMMAND_TYPE.UPLOAD;

public class FileDTOConverter implements DTOConverter {
  private final FileDownloadDTOConverter fileDownloadDTOConverter;
  private final FileUploadDTOConverter fileUploadDTOConverter;

  private final FileListFileDTOConverter listFileDTOConverter;

  public FileDTOConverter() throws JAXBException {
    this.fileDownloadDTOConverter = new FileDownloadDTOConverter();
    this.fileUploadDTOConverter = new FileUploadDTOConverter();
    this.listFileDTOConverter = new FileListFileDTOConverter();
  }

  @Override
  public String serialize(DTOInterfaces.REQUEST_DTO dto) throws UnableToSerialize {
    var type = dto.getDTOType();
    if (type == RequestDTO.DTO_TYPE.COMMAND) {
      var commandType = ((DTOInterfaces.COMMAND_DTO) dto).getCommandType();
      if (commandType == UPLOAD) {
        return fileUploadDTOConverter.serialize(dto);
      } else if (commandType == RequestDTO.COMMAND_TYPE.DOWNLOAD) {
        return fileDownloadDTOConverter.serialize(dto);
      } else if (commandType == RequestDTO.COMMAND_TYPE.LISTFILE) {
        return listFileDTOConverter.serialize(dto);
      } else throw new UnableToSerialize("unrecognized dto");
    } else if (type == RequestDTO.DTO_TYPE.EVENT) {
      if (((DTOInterfaces.EVENT_DTO) dto).getEventType() == RequestDTO.EVENT_TYPE.FILE) {
        return fileUploadDTOConverter.serialize(dto);
      } else throw new UnableToSerialize("unrecognized dto");
    } else throw new UnableToSerialize("support only DTO_TYPE=COMMAND/EVENT");
  }

  @Override
  public DTOInterfaces.REQUEST_DTO deserialize(Document root) throws UnableToDeserialize {
    var type = DTOConverterManagerInterface.getDTOType(root);
    if (type == RequestDTO.DTO_TYPE.COMMAND) {
      var commandType = DTOConverterManagerInterface.getDTOCommand(root);
      if (commandType == UPLOAD) {
        return fileUploadDTOConverter.deserialize(root);
      } else if (commandType == RequestDTO.COMMAND_TYPE.DOWNLOAD) {
        return fileDownloadDTOConverter.deserialize(root);
      } else throw new UnableToDeserialize("unrecognized dto");
    } else if (type == RequestDTO.DTO_TYPE.EVENT) {
      var eventType = DTOConverterManagerInterface.getDTOEvent(root);
      if (eventType == RequestDTO.EVENT_TYPE.FILE) {
        return fileUploadDTOConverter.deserialize(root);
      } else throw new UnableToDeserialize("unrecognized dto");
    } else throw new UnableToDeserialize("support only DTO_TYPE=COMMAND/EVENT");
  }

  public FileDownloadDTOConverter getFileDownloadDTOConverter() {
    return fileDownloadDTOConverter;
  }

  public FileUploadDTOConverter getFileUploadDTOConverter() {
    return fileUploadDTOConverter;
  }

  public FileListFileDTOConverter getListFileDTOConverter() {
    return listFileDTOConverter;
  }
}