package dto.subtypes;

import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.exceptions.UnsupportedDTOType;
import dto.interfaces.DTOConverter;
import dto.interfaces.DTOConverterManagerInterface;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import org.w3c.dom.Document;

import java.util.Arrays;
import java.util.Objects;

public class FileDTO {
  public static class FileDTOConverter implements DTOConverter {
    private final FileDownloadDTOConverter fileDownloadDTOConverter;
    private final FileUploadDTOConverter fileUploadDTOConverter;

    public FileDTOConverter() throws JAXBException {
      this.fileDownloadDTOConverter = new FileDownloadDTOConverter();
      this.fileUploadDTOConverter = new FileUploadDTOConverter();
    }

    @Override
    public String serialize(RequestDTO dto) throws UnableToSerialize {
      var type = dto.getDTOType();
      if (type == RequestDTO.DTO_TYPE.COMMAND) {
        var commandType = ((Command) dto).getCommandType();
        if (commandType == RequestDTO.COMMAND_TYPE.UPLOAD) {
          return fileUploadDTOConverter.serialize(dto);
        } else if (commandType == RequestDTO.COMMAND_TYPE.DOWNLOAD) {
          return fileDownloadDTOConverter.serialize(dto);
        } else throw new UnsupportedDTOType("unrecognized dto");
      } else if (type == RequestDTO.DTO_TYPE.EVENT) {
        if (((RequestDTO.BaseEvent) dto).getEventType() == RequestDTO.EVENT_TYPE.FILE) {
          return fileUploadDTOConverter.serialize(dto);
        } else throw new UnsupportedDTOType("unrecognized dto");
      } else throw new UnsupportedDTOType("support only DTO_TYPE=COMMAND/EVENT");
    }

    @Override
    public RequestDTO deserialize(Document root) throws UnableToDeserialize {
      var type = DTOConverterManagerInterface.getDTOType(root);
      if (type == RequestDTO.DTO_TYPE.COMMAND) {
        var commandType = DTOConverterManagerInterface.getDTOCommand(root);
        if (commandType == RequestDTO.COMMAND_TYPE.UPLOAD) {
          return fileUploadDTOConverter.deserialize(root);
        } else if (commandType == RequestDTO.COMMAND_TYPE.DOWNLOAD) {
          return fileDownloadDTOConverter.deserialize(root);
        } else throw new UnsupportedDTOType("unrecognized dto");
      } else if (type == RequestDTO.DTO_TYPE.EVENT) {
        var eventType = DTOConverterManagerInterface.getDTOEvent(root);
        if (eventType == RequestDTO.EVENT_TYPE.FILE) {
          return fileUploadDTOConverter.deserialize(root);
        } else throw new UnsupportedDTOType("unrecognized dto");
      } else throw new UnsupportedDTOType("support only DTO_TYPE=COMMAND/EVENT");
    }

    public FileDownloadDTOConverter getFileDownloadDTOConverter() {
      return fileDownloadDTOConverter;
    }

    public FileUploadDTOConverter getFileUploadDTOConverter() {
      return fileUploadDTOConverter;
    }
  }

  public static class FileUploadDTOConverter extends RequestDTO.BaseDTOConverter {
    public FileUploadDTOConverter() throws JAXBException {
      super(JAXBContext.newInstance(Event.class, UploadCommand.class, UploadSuccess.class, Error.class));
    }
  }

  public static class FileDownloadDTOConverter extends RequestDTO.BaseDTOConverter {
    public FileDownloadDTOConverter() throws JAXBException {
      super(JAXBContext.newInstance(DownloadSuccess.class, DownloadCommand.class, Error.class));
    }
  }

  private static class Command extends RequestDTO.BaseCommand {
    public Command(COMMAND_TYPE commandType) {
      super(DTO_SECTION.FILE, commandType);
    }

    public Command() {
      super(DTO_SECTION.FILE, null);
    }
  }

  @XmlType(name = "ploadfilecommand"/*, propOrder = {"name", "mimeType", "encoding", "content"}*/)
  @XmlRootElement(name = "command")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class UploadCommand extends Command implements DTOInterfaces.NAME, DTOInterfaces.MIME_TYPE, DTOInterfaces.ENCODING, DTOInterfaces.CONTENT {
    private String name;
    private String mimeType;
    private String encoding;
    private byte[] content;

    public UploadCommand() {
      super(COMMAND_TYPE.UPLOAD);
    }

    public UploadCommand(String name, String mimeType, String encoding, byte[] content) {
      this();
      this.name = name;
      this.mimeType = mimeType;
      this.encoding = encoding;
      this.content = content;
    }

    @Override
//    @XmlElement(name = "name")
    public String getName() {
      return name;
    }

    @Override
//    @XmlElement(name = "mimeType")
    public String getMimeType() {
      return mimeType;
    }

    @Override
//    @XmlElement(name = "encoding")
    public String getEncoding() {
      return encoding;
    }

    @Override
//    @XmlElement(name = "content")
    public byte[] getContent() {
      return content;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof UploadCommand that)) return false;
      if (!super.equals(o)) return false;
      return Objects.equals(name, that.name) && Objects.equals(mimeType, that.mimeType) && Objects.equals(encoding, that.encoding) && Objects.deepEquals(content, that.content);
    }

    @Override
    public int hashCode() {
      return Objects.hash(super.hashCode(), name, mimeType, encoding, Arrays.hashCode(content));
    }
  }

  @XmlType(name = "ownloadfilecommand")
  @XmlRootElement(name = "command")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class DownloadCommand extends Command implements DTOInterfaces.ID {
    private String id;

    public DownloadCommand() {
      super(COMMAND_TYPE.DOWNLOAD);
    }

    public DownloadCommand(String id) {
      this();
      this.id = id;
    }

    @Override
//    @XmlElement(name = "id")
    public String getId() {
      return id;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof DownloadCommand that)) return false;
      if (!super.equals(o)) return false;
      return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
      return Objects.hash(super.hashCode(), id);
    }
  }

  @XmlType(propOrder = {"id", "from", "name", "size", "mimeType"})
  @XmlRootElement(name = "event")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Event extends RequestDTO.BaseEvent implements DTOInterfaces.ID, DTOInterfaces.FROM, DTOInterfaces.NAME, DTOInterfaces.SIZE, DTOInterfaces.MIME_TYPE {
    private String id;
    private String from;
    private String name;
    private long size;
    private String mimeType;

    public Event() {
      super(EVENT_TYPE.FILE, DTO_SECTION.FILE);
    }

    public Event(String id, String from, String name, long size, String mimeType) {
      this();
      this.id = id;
      this.from = from;
      this.name = name;
      this.size = size;
      this.mimeType = mimeType;
    }

    @Override
    public String getId() {
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
      if (!super.equals(o)) return false;
      return size == event.size && Objects.equals(id, event.id) && Objects.equals(from, event.from) && Objects.equals(name, event.name) && Objects.equals(mimeType, event.mimeType);
    }

    @Override
    public int hashCode() {
      return Objects.hash(super.hashCode(), id, from, name, size, mimeType);
    }
  }

  @XmlType(name = "uploadsuccess")
  @XmlRootElement(name = "success")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class UploadSuccess extends RequestDTO.BaseSuccessResponse implements DTOInterfaces.ID {
    private String id;

    public UploadSuccess() {
      super(DTO_SECTION.FILE);
    }

    public UploadSuccess(String id) {
      this();
      this.id = id;
    }

    @Override
//    @XmlElement(name = "id")
    public String getId() {
      return id;
    }


  }

  @XmlType(name = "downloadsuccess", propOrder = {"id", "name", "mimeType", "encoding", "content"})
  @XmlRootElement(name = "success")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class DownloadSuccess extends RequestDTO.BaseSuccessResponse implements DTOInterfaces.ID, DTOInterfaces.NAME, DTOInterfaces.MIME_TYPE, DTOInterfaces.ENCODING, DTOInterfaces.CONTENT {
    private String id;
    private String name;
    private String mimeType;
    private String encoding;
    private byte[] content;

    public DownloadSuccess() {
      super(DTO_SECTION.FILE);
    }

    public DownloadSuccess(String id) {
      this();
      this.id = id;
    }

    public DownloadSuccess(String id, String name, String mimeType, String encoding, byte[] content) {
      this();
      this.id = id;
      this.name = name;
      this.mimeType = mimeType;
      this.encoding = encoding;
      this.content = content;
    }


    @Override
//    @XmlElement(name = "id")
    public String getId() {
      return id;
    }

    @Override
//    @XmlElement(name = "content")
    public byte[] getContent() {
      return content;
    }

    @Override
//    @XmlElement(name = "encoding")
    public String getEncoding() {
      return encoding;
    }

    @Override
//    @XmlElement(name = "mimeType")
    public String getMimeType() {
      return mimeType;
    }

    @Override
//    @XmlElement(name = "name")
    public String getName() {
      return name;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof DownloadSuccess that)) return false;
      if (!super.equals(o)) return false;
      return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(mimeType, that.mimeType) && Objects.equals(encoding, that.encoding) && Objects.deepEquals(content, that.content);
    }

    @Override
    public int hashCode() {
      return Objects.hash(super.hashCode(), id, name, mimeType, encoding, Arrays.hashCode(content));
    }

    @Override
    public String toString() {
      return "DownloadSuccess{" +
              "id='" + id + '\'' +
              ", name='" + name + '\'' +
              ", mimeType='" + mimeType + '\'' +
              ", encoding='" + encoding + '\'' +
              ", content=" + Arrays.toString(content) +
              '}';
    }
  }

  @XmlType(name = "fileerror")
  @XmlRootElement(name = "error")
  public static class Error extends RequestDTO.BaseErrorResponse {
    public Error() {
      super(DTO_SECTION.FILE);
    }

    public Error(String message) {
      super(DTO_SECTION.FILE, message);
    }
  }
}
