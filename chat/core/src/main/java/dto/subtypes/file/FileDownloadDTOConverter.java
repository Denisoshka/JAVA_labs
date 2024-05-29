package dto.subtypes.file;

import dto.BaseDTOConverter;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

public class FileDownloadDTOConverter extends BaseDTOConverter {
  public FileDownloadDTOConverter() throws JAXBException {
    super(JAXBContext.newInstance(FileDownloadSuccess.class, FileDownloadCommand.class, FileError.class));
  }
}
