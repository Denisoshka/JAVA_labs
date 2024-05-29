package dto.subtypes.file;

import dto.BaseDTOConverter;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;


public class FileUploadDTOConverter extends BaseDTOConverter {
  public FileUploadDTOConverter() throws JAXBException {
    super(JAXBContext.newInstance(FileEvent.class, UploadCommand.class, UploadSuccess.class, FileError.class));
  }
}