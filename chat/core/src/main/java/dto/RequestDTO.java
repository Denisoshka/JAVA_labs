package dto;

import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.Objects;

public abstract class RequestDTO implements DTOInterfaces.REQUEST_DTO {
  public enum DTO_TYPE {
    COMMAND("command"),
    EVENT("event"),
    RESPONSE("response"),
    SUCCESS("response"),
    ERROR("error");;

    public String getName() {
      return name;
    }

    private final String name;

    DTO_TYPE(String name) {
      this.name = name;
    }
  }

  public enum DTO_SECTION {
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

  public enum COMMAND_TYPE implements DTOInterfaces.DTO_SECTION {
    MESSAGE("message", DTO_SECTION.MESSAGE),
    LOGOUT("logout", DTO_SECTION.LOGOUT),
    LOGIN("login", DTO_SECTION.LOGIN),
    LIST("list", DTO_SECTION.LIST),
    UPLOAD("upload", DTO_SECTION.FILE),
    DOWNLOAD("download", DTO_SECTION.FILE),
    LISTFILE("listfile", DTO_SECTION.FILE);

    private final String name;
    private final DTO_SECTION section;

    COMMAND_TYPE(String name, DTO_SECTION section) {
      this.name = name;
      this.section = section;
    }

    public String getName() {
      return name;
    }

    @Override
    public DTO_SECTION geDTOSection() {
      return section;
    }
  }

  public enum EVENT_TYPE implements DTOInterfaces.DTO_SECTION {
    MESSAGE("message", DTO_SECTION.MESSAGE),
    USERLOGIN("userlogin", DTO_SECTION.LOGIN),
    USERLOGOUT("userlogout", DTO_SECTION.LOGOUT),
    FILE("file", DTO_SECTION.FILE),
    ;
    private final DTO_SECTION section;
    private final String name;

    EVENT_TYPE(String name, DTO_SECTION section) {
      this.name = name;
      this.section = section;
    }

    public String getName() {
      return name;
    }

    @Override
    public DTO_SECTION geDTOSection() {
      return section;
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
}
