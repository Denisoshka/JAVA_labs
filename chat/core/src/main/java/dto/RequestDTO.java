package dto;

import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.interfaces.DTOInterfaces;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.io.StringWriter;
import java.util.Objects;

public class RequestDTO implements DTOInterfaces.DTO_TYPE, DTOInterfaces.DTO_SECTION {
  public static class DTOConverter implements dto.interfaces.DTOConverter {
    JAXBContext context;
    Unmarshaller unmarshaller;
    Marshaller marshaller;

    public DTOConverter(JAXBContext context) throws JAXBException {
      this.context = context;
      this.unmarshaller = context.createUnmarshaller();
      this.marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
    }

    @Override
    public String serialize(RequestDTO dto) throws UnableToSerialize {
      try {
        StringWriter writer = new StringWriter();
        marshaller.marshal(dto, writer);
        return writer.toString();
      } catch (JAXBException e) {
        throw new UnableToSerialize(e);
      }
    }

    @Override
    public RequestDTO deserialize(Node root) throws UnableToDeserialize {
      try {
        return (RequestDTO) unmarshaller.unmarshal(root);
      } catch (JAXBException e) {
        throw new UnableToDeserialize(e);
      }
    }
  }

  public enum DTO_TYPE {
    COMMAND("command"),
    EVENT("event"),
    RESPONSE("response"),
    ;
    final String DTOName;

    DTO_TYPE(String DTOName) {
      this.DTOName = DTOName;
    }
  }

  public enum DTO_SECTION {
    MESSAGE("message"),
    LOGOUT("logout"),
    LOGIN("login"),
    LIST("list"),
    ;

    DTO_SECTION(String COMMANDName) {
      this.COMMANDName = COMMANDName;
    }

    private final String COMMANDName;

    public String getCOMMANDName() {
      return this.COMMANDName;
    }
  }

  final DTO_TYPE DTOType;
  final DTO_SECTION DTOSection;

  public RequestDTO(DTO_TYPE DTOType, DTO_SECTION DTOSection) {
    this.DTOSection = DTOSection;
    this.DTOType = DTOType;
  }

  @Override
  public DTO_TYPE getDTOType() {
    return DTOType;
  }

  @Override
  public DTO_SECTION getCommandType() {
    return DTOSection;
  }

  //  @XmlRootElement(name = "command")
  public static class BaseCommand extends RequestDTO implements DTOInterfaces.NAME_ATTRIBUTE {
    private String nameAttribute;

    public BaseCommand(DTO_SECTION commandType) {
      super(DTO_TYPE.COMMAND, commandType);
      this.nameAttribute = commandType.getCOMMANDName();
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
  }

  public static class BaseEvent extends RequestDTO implements DTOInterfaces.EVENT_TYPE, DTOInterfaces.NAME_ATTRIBUTE {
    public enum EVENT_TYPE {
      MESSAGE("message"),
      USER_LOGIN("userlogin"),
      USER_LOGOUT("userlogout"),
      ;

      private final String EVENTName;

      EVENT_TYPE(String name) {
        this.EVENTName = name;
      }

      public String getEVENTName() {
        return EVENTName;
      }
    }

    final EVENT_TYPE eventType;
    private final String nameAttribute;

    public BaseEvent(EVENT_TYPE DTOType, DTO_SECTION DTOSection) {
      super(DTO_TYPE.EVENT, DTOSection);
      this.eventType = DTOType;
      this.nameAttribute = DTOType.getEVENTName();
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

  //  @XmlType(name = "baseresponse")
  public static class BaseResponse extends RequestDTO implements DTOInterfaces.RESPONSE_TYPE {
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

    private final RESPONSE_TYPE responseType;

    public BaseResponse(DTO_SECTION DTOSection, RESPONSE_TYPE responseType) {
      super(DTO_TYPE.RESPONSE, DTOSection);
      this.responseType = responseType;
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
  public static class BaseSuccessResponse extends BaseResponse {
    public BaseSuccessResponse(DTO_SECTION DTOSection) {
      super(DTOSection, RESPONSE_TYPE.SUCCESS);
    }
  }

  //  @XmlType(name = "baseerrorresponse")
  public static class BaseErrorResponse extends BaseResponse implements DTOInterfaces.MESSAGE {
    String message;

    public BaseErrorResponse(DTO_SECTION DTOSection) {
      super(DTOSection, RESPONSE_TYPE.ERROR);
    }

    public BaseErrorResponse(DTO_SECTION DTOSection, String message) {
      super(DTOSection, RESPONSE_TYPE.ERROR);
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
