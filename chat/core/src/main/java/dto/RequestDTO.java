package dto;

import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.interfaces.DTOConverter;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import org.w3c.dom.Document;

import java.io.StringWriter;
import java.util.Objects;

public class RequestDTO implements DTOInterfaces.DTO_TYPE, DTOInterfaces.DTO_SECTION {
  public static class BaseDTOConverter implements DTOConverter {
    final JAXBContext context;
    final Unmarshaller unmarshaller;
    final Marshaller marshaller;

    public BaseDTOConverter(JAXBContext context) throws JAXBException {
      this.context = context;
      this.unmarshaller = context.createUnmarshaller();
      this.marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
    }

    @Override
    public String serialize(RequestDTO dto) throws UnableToSerialize {
      try {
        StringWriter writer = new StringWriter();
        synchronized (marshaller) {
          marshaller.marshal(dto, writer);
        }
        return writer.toString();
      } catch (JAXBException e) {
        throw new UnableToSerialize(e);
      }
    }

    @Override
    public RequestDTO deserialize(Document root) throws UnableToDeserialize {
      try {
        synchronized (unmarshaller) {
          return (RequestDTO) unmarshaller.unmarshal(root);
        }
      } catch (JAXBException e) {
        throw new UnableToDeserialize(e);
      }
    }
  }

  public enum DTO_TYPE {
    COMMAND("command"),
    EVENT("event"),
    RESPONSE("response"),
    SUCCESS("response"),
    ERROR("error");;
    final String DTOName;

    DTO_TYPE(String DTOName) {
      this.DTOName = DTOName;
    }
  }

  public enum DTO_SECTION {
    BASE("base"),
    MESSAGE("message"),
    LOGOUT("logout"),
    LOGIN("login"),
    LIST("list"),
    FILE("file");

    DTO_SECTION(String COMMANDName) {
      this.COMMANDName = COMMANDName;
    }

    private final String COMMANDName;

    public String getCOMMANDName() {
      return this.COMMANDName;
    }
  }

  public enum COMMAND_TYPE {
    MESSAGE("message"),
    LOGOUT("logout"),
    LOGIN("login"),
    LIST("list"),
    UPLOAD("upload"),
    DOWNLOAD("download"),
    LISTFILE("listfile");


    private final String type;

    COMMAND_TYPE(String type) {
      this.type = type;
    }

    public String getType() {
      return type;
    }
  }

  public enum EVENT_TYPE {
    BASE("base"),
    MESSAGE("message"),
    USERLOGIN("userlogin"),
    USERLOGOUT("userlogout"),
    FILE("file"),
    ;

    private final String EVENTName;

    EVENT_TYPE(String name) {
      this.EVENTName = name;
    }

    public String getEVENTName() {
      return EVENTName;
    }
  }

  public enum RESPONSE_TYPE {
    SUCCESS("success"),
    ERROR("error"),
    ;

    private final String RESPONSEName;

    RESPONSE_TYPE(String RESPONSEName) {
      this.RESPONSEName = RESPONSEName;
    }

    public String getRESPONSEName() {
      return RESPONSEName;
    }
  }

  @XmlTransient
  protected DTO_TYPE DTOType;
  @XmlTransient
  protected DTO_SECTION DTOSection;

  public RequestDTO(DTO_TYPE DTOType, DTO_SECTION DTOSection) {
    this.DTOSection = DTOSection;
    this.DTOType = DTOType;
  }

  public RequestDTO() {
  }

  @Override
  public DTO_TYPE getDTOType() {
    return DTOType;
  }

  @Override
  public DTO_SECTION geDTOSection() {
    return DTOSection;
  }

  //  @XmlRootElement(name = "command")
  public static class BaseCommand extends RequestDTO implements DTOInterfaces.NAME_ATTRIBUTE, DTOInterfaces.COMMAND_TYPE {
    @XmlTransient
    protected COMMAND_TYPE commandType;
    protected String nameAttribute;

    public BaseCommand(DTO_SECTION dtoSection, COMMAND_TYPE commandType) {
      super(DTO_TYPE.COMMAND, dtoSection);
      this.commandType = commandType;
      this.nameAttribute = commandType.getType();
    }

    public BaseCommand() {
      super(DTO_TYPE.COMMAND, null);
    }

    @Override
    @XmlAttribute(name = "name")
    public String getNameAttribute() {
      return nameAttribute;
    }

    public void setNameAttribute(String nameAttribute) {
      this.nameAttribute = nameAttribute;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof BaseCommand that)) return false;
      return Objects.equals(nameAttribute, that.nameAttribute);
    }

    @Override
    public int hashCode() {
      return Objects.hash(nameAttribute);
    }

    @Override
    public COMMAND_TYPE getCommandType() {
      return commandType;
    }
  }

  public static class BaseEvent extends RequestDTO implements DTOInterfaces.EVENT_TYPE, DTOInterfaces.NAME_ATTRIBUTE {
    @XmlTransient
    protected EVENT_TYPE eventType;
    protected String nameAttribute;

    public BaseEvent(EVENT_TYPE DTOType, DTO_SECTION DTOSection) {
      super(DTO_TYPE.EVENT, DTOSection);
      this.eventType = DTOType;
      this.nameAttribute = DTOType.getEVENTName();
    }

    public BaseEvent() {
    }

    @Override
    public EVENT_TYPE getEventType() {
      return eventType;
    }

    @Override
    @XmlAttribute(name = "name")
    public String getNameAttribute() {
      return nameAttribute;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof BaseEvent baseEvent)) return false;
      return eventType == baseEvent.eventType && Objects.equals(nameAttribute, baseEvent.nameAttribute);
    }

    @Override
    public int hashCode() {
      return Objects.hash(eventType, nameAttribute);
    }
  }

  public static class BaseResponse extends RequestDTO implements DTOInterfaces.RESPONSE_TYPE {
    @XmlTransient
    protected RESPONSE_TYPE responseType;

    public BaseResponse(DTO_SECTION DTOSection, RESPONSE_TYPE responseType) {
      super(DTO_TYPE.RESPONSE, DTOSection);
      this.responseType = responseType;
    }

    public BaseResponse() {
      super(DTO_TYPE.RESPONSE, null);
    }

    @Override
    public RESPONSE_TYPE getResponseType() {
      return responseType;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof BaseResponse that)) return false;
      return responseType == that.responseType;
    }

    @Override
    public int hashCode() {
      return Objects.hash(responseType);
    }
  }

  //  @XmlType(name = "basesuccessresponse")
//  @XmlRootElement(name = "success")
  public static class BaseSuccessResponse extends BaseResponse {
    public BaseSuccessResponse(DTO_SECTION DTOSection) {
      super(DTOSection, RESPONSE_TYPE.SUCCESS);
    }

    public BaseSuccessResponse() {
      this(DTO_SECTION.BASE);
    }
  }

  @XmlType(name = "baseerrorresponse")
  public static class BaseErrorResponse extends BaseResponse implements DTOInterfaces.MESSAGE {
    String message;

    public BaseErrorResponse(DTO_SECTION DTOSection) {
      super(DTOSection, RESPONSE_TYPE.ERROR);
    }

    public BaseErrorResponse() {
      this(DTO_SECTION.BASE);
    }

    public BaseErrorResponse(DTO_SECTION DTOSection, String message) {
      super(DTOSection, RESPONSE_TYPE.ERROR);
      this.message = message;
    }

    public BaseErrorResponse(String message) {
      this();
      this.message = message;
    }

    @Override
    @XmlElement(name = "message")
    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof BaseErrorResponse that)) return false;
      return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
      return Objects.hash(message);
    }
  }
}
