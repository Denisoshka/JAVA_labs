package dto.subtypes.file;

import dto.BaseDTOConverter;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

public class FileListFileDTOConverter extends BaseDTOConverter {
  public FileListFileDTOConverter() throws JAXBException {
    super(JAXBContext.newInstance(ListFileCommand.class, ListFileSuccess.class, FileError.class));
  }
}
