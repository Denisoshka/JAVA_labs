package dto.subtypes.login;

import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Objects;

@XmlRootElement(name = "command")
@XmlAccessorType(XmlAccessType.FIELD)
public  class LoginCommand implements DTOInterfaces.COMMAND_DTO, DTOInterfaces.NAME, DTOInterfaces.PASSWORD {
  @XmlAttribute(name = "name")
  private final String nameAttribute = RequestDTO.COMMAND_TYPE.LOGIN.getName();
  private String name;
  private String password;

  public LoginCommand() {
  }

  public LoginCommand(String name, String password) {
    this.name = name;
    this.password = password;
  }

  @Override
  public String getNameAttribute() {
    return nameAttribute;
  }

  @Override
  public RequestDTO.COMMAND_TYPE getCommandType() {
    return RequestDTO.COMMAND_TYPE.LOGIN;
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return RequestDTO.DTO_SECTION.LOGIN;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof LoginCommand loginCommand)) return false;
    return Objects.equals(nameAttribute, loginCommand.nameAttribute) && Objects.equals(name, loginCommand.name) && Objects.equals(password, loginCommand.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nameAttribute, name, password);
  }
}

