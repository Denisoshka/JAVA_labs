package dto.subtypes;

import dto.BaseDTOConverter;
import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.interfaces.DTOConverter;
import dto.interfaces.DTOConverterManagerInterface;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.annotation.*;
import org.w3c.dom.Document;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static dto.RequestDTO.COMMAND_TYPE.*;
import static dto.RequestDTO.DTO_SECTION.FILE;

public class FileDTO {
  public static class FileDTOConverter implements DTOConverter {
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

  public static class FileUploadDTOConverter extends BaseDTOConverter {
    public FileUploadDTOConverter() throws JAXBException {
      super(JAXBContext.newInstance(Event.class, UploadCommand.class, UploadSuccess.class, Error.class));
    }
  }

  public static class FileDownloadDTOConverter extends BaseDTOConverter {
    public FileDownloadDTOConverter() throws JAXBException {
      super(JAXBContext.newInstance(DownloadSuccess.class, DownloadCommand.class, Error.class));
    }
  }

  public static class FileListFileDTOConverter extends BaseDTOConverter {
    public FileListFileDTOConverter() throws JAXBException {
      super(JAXBContext.newInstance(ListFileCommand.class, ListFileSuccess.class, Error.class));
    }
  }

  @XmlType(name = "uploadfilecommand"/*, propOrder = {"name", "mimeType", "encoding", "content"}*/)
  @XmlRootElement(name = "command")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class UploadCommand implements DTOInterfaces.COMMAND_DTO, DTOInterfaces.NAME, DTOInterfaces.MIME_TYPE, DTOInterfaces.ENCODING, DTOInterfaces.CONTENT {
    @XmlAttribute(name = "name")
    private final String nameAttribute = UPLOAD.getName();
    private String name;
    private String mimeType;
    private String encoding;
    private byte[] content;

    public UploadCommand() {
    }

    public UploadCommand(String name, String mimeType, String encoding, byte[] content) {
      this.name = name;
      this.mimeType = mimeType;
      this.encoding = encoding;
      this.content = content;
    }

    @Override
    public String getNameAttribute() {
      return nameAttribute;
    }

    @Override
    public RequestDTO.COMMAND_TYPE getCommandType() {
      return UPLOAD;
    }

    @Override
    public RequestDTO.DTO_SECTION getDTOSection() {
      return UPLOAD.geDTOSection();
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public String getMimeType() {
      return mimeType;
    }

    @Override
    public String getEncoding() {
      return encoding;
    }

    @Override
    public byte[] getContent() {
      return content;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof UploadCommand that)) return false;
      return Objects.equals(nameAttribute, that.nameAttribute) && Objects.equals(name, that.name) && Objects.equals(mimeType, that.mimeType) && Objects.equals(encoding, that.encoding) && Objects.deepEquals(content, that.content);
    }

    @Override
    public int hashCode() {
      return Objects.hash(nameAttribute, name, mimeType, encoding, Arrays.hashCode(content));
    }
  }

  @XmlType(name = "ownloadfilecommand")
  @XmlRootElement(name = "command")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class DownloadCommand implements DTOInterfaces.COMMAND_DTO, DTOInterfaces.ID {
    @XmlAttribute(name = "name")
    private final String nameAttribute = DOWNLOAD.getName();
    private Long id;

    public DownloadCommand() {
    }

    public DownloadCommand(Long id) {
      this.id = id;
    }

    @Override
    public String getNameAttribute() {
      return nameAttribute;
    }

    @Override
    public RequestDTO.COMMAND_TYPE getCommandType() {
      return DOWNLOAD;
    }

    @Override
    public RequestDTO.DTO_SECTION getDTOSection() {
      return DOWNLOAD.geDTOSection();
    }

    @Override
    public Long getId() {
      return id;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof DownloadCommand command)) return false;
      return Objects.equals(nameAttribute, command.nameAttribute) && Objects.equals(id, command.id);
    }

    @Override
    public int hashCode() {
      return Objects.hash(nameAttribute, id);
    }
  }

  @XmlRootElement(name = "event")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Event implements DTOInterfaces.EVENT_DTO, DTOInterfaces.ID, DTOInterfaces.FROM, DTOInterfaces.NAME, DTOInterfaces.SIZE, DTOInterfaces.MIME_TYPE {
    @XmlAttribute(name = "name")
    private final String nameAttribute = RequestDTO.EVENT_TYPE.FILE.getName();
    private Long id;
    private String from;
    private String name;
    private long size;
    private String mimeType;

    public Event() {
    }

    public Event(Long id, String from, String name, long size, String mimeType) {
      this.id = id;
      this.from = from;
      this.name = name;
      this.size = size;
      this.mimeType = mimeType;
    }

    @Override
    public RequestDTO.EVENT_TYPE getEventType() {
      return RequestDTO.EVENT_TYPE.FILE;
    }

    @Override
    public String getNameAttribute() {
      return RequestDTO.EVENT_TYPE.FILE.getName();
    }

    @Override
    public RequestDTO.DTO_SECTION getDTOSection() {
      return RequestDTO.EVENT_TYPE.FILE.geDTOSection();
    }

    @Override
    public Long getId() {
      return id;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public String getFrom() {
      return from;
    }

    @Override
    public long getSize() {
      return size;
    }

    @Override
    public String getMimeType() {
      return mimeType;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Event event)) return false;
      return size == event.size && Objects.equals(nameAttribute, event.nameAttribute) && Objects.equals(id, event.id) && Objects.equals(from, event.from) && Objects.equals(name, event.name) && Objects.equals(mimeType, event.mimeType);
    }

    @Override
    public int hashCode() {
      return Objects.hash(nameAttribute, id, from, name, size, mimeType);
    }
  }

  @XmlType(name = "uploadsuccess")
  @XmlRootElement(name = "success")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class UploadSuccess implements DTOInterfaces.SUCCESS_RESPONSE_DTO, DTOInterfaces.ID {
    private Long id;

    public UploadSuccess() {
    }

    public UploadSuccess(Long id) {
      this.id = id;
    }

    @Override
    public Long getId() {
      return id;
    }

    @Override
    public RequestDTO.DTO_SECTION getDTOSection() {
      return FILE;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof UploadSuccess that)) return false;
      return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(id);
    }
  }

  @XmlType(name = "downloadsuccess"/*, propOrder = {"id", "name", "mimeType", "encoding", "content"}*/)
  @XmlRootElement(name = "success")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class DownloadSuccess implements DTOInterfaces.SUCCESS_RESPONSE_DTO, DTOInterfaces.ID, DTOInterfaces.NAME, DTOInterfaces.MIME_TYPE, DTOInterfaces.ENCODING, DTOInterfaces.CONTENT {
    private Long id;
    private String name;
    private String mimeType;
    private String encoding;
    private byte[] content;

    public DownloadSuccess() {
    }

    public DownloadSuccess(Long id, String name, String mimeType, String encoding, byte[] content) {
      this.id = id;
      this.name = name;
      this.mimeType = mimeType;
      this.encoding = encoding;
      this.content = content;
    }

    @Override
    public RequestDTO.DTO_SECTION getDTOSection() {
      return FILE;
    }

    @Override
    public Long getId() {
      return id;
    }

    @Override
    public byte[] getContent() {
      return content;
    }

    @Override
    public String getEncoding() {
      return encoding;
    }

    @Override
    public String getMimeType() {
      return mimeType;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof DownloadSuccess that)) return false;
      return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(mimeType, that.mimeType) && Objects.equals(encoding, that.encoding) && Objects.deepEquals(content, that.content);
    }

    @Override
    public int hashCode() {
      return Objects.hash(id, name, mimeType, encoding, Arrays.hashCode(content));
    }
  }

  @XmlType(name = "fileerror")
  @XmlRootElement(name = "error")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Error implements DTOInterfaces.ERROR_RESPONSE_DTO {
    private String message;

    public Error() {
    }

    public Error(String message) {
      this.message = message;
    }

    @Override
    public String getMessage() {
      return message;
    }

    @Override
    public RequestDTO.DTO_SECTION getDTOSection() {
      return FILE;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Error error)) return false;
      return Objects.equals(message, error.message);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(message);
    }
  }

  @XmlRootElement(name = "command")
  public static class ListFileCommand implements DTOInterfaces.COMMAND_DTO {
    @XmlAttribute(name = "name")
    private final String nameAttribute = RequestDTO.COMMAND_TYPE.LISTFILE.getName();

    @Override
    public String getNameAttribute() {
      return nameAttribute;
    }

    @Override
    public RequestDTO.COMMAND_TYPE getCommandType() {
      return LISTFILE;
    }

    @Override
    public RequestDTO.DTO_SECTION getDTOSection() {
      return LISTFILE.geDTOSection();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof ListFileCommand that)) return false;
      return Objects.equals(nameAttribute, that.nameAttribute);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(nameAttribute);
    }
  }

  @XmlType(name = "fileentity")
  @XmlRootElement(name = "file")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class FileEntity implements DTOInterfaces.ID, DTOInterfaces.FROM, DTOInterfaces.NAME, DTOInterfaces.SIZE, DTOInterfaces.MIME_TYPE {
    private Long id;
    private String from;
    private String name;
    private long size;
    private String mimeType;

    public FileEntity(Long id, String from, String name, long size, String mimeType) {
      this.id = id;
      this.from = from;
      this.name = name;
      this.size = size;
      this.mimeType = mimeType;
    }

    public FileEntity() {
    }

    @Override
    public String getFrom() {
      return from;
    }

    @Override
    public Long getId() {
      return id;
    }

    @Override
    public String getMimeType() {
      return mimeType;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public long getSize() {
      return size;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof FileEntity that)) return false;
      return size == that.size && Objects.equals(id, that.id) && Objects.equals(from, that.from) && Objects.equals(name, that.name) && Objects.equals(mimeType, that.mimeType);
    }

    @Override
    public int hashCode() {
      return Objects.hash(id, from, name, size, mimeType);
    }
  }

  @XmlRootElement(name = "success")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class ListFileSuccess implements DTOInterfaces.SUCCESS_RESPONSE_DTO, DTOInterfaces.FILES {
    @XmlElementWrapper(name = "files")
    @XmlElement(name = "file")
    private List<FileEntity> files;

    public ListFileSuccess() {
    }

    public ListFileSuccess(List<FileEntity> files) {
      this.files = files;
    }

    public List<FileEntity> getFiles() {
      return files;
    }

    public void setFiles(List<FileEntity> files) {
      this.files = files;
    }

    @Override
    public RequestDTO.DTO_SECTION getDTOSection() {
      return RequestDTO.DTO_SECTION.FILE;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof ListFileSuccess that)) return false;
      return Objects.equals(files, that.files);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(files);
    }
  }
}
