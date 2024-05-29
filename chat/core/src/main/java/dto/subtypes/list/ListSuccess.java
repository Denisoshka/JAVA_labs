package dto.subtypes.list;

import dto.DataDTO;
import dto.RequestDTO;
import dto.interfaces.DTOInterfaces;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;
import java.util.Objects;

@XmlRootElement(name = "success")
public class ListSuccess implements DTOInterfaces.SUCCESS_RESPONSE_DTO, DTOInterfaces.USERS {
  private List<DataDTO.User> users;

  public ListSuccess() {
  }

  public ListSuccess(List<DataDTO.User> users) {
    this.users = users;
  }

  @Override
  @XmlElementWrapper(name = "users")
  @XmlElement(name = "user")
  public List<DataDTO.User> getUsers() {
    return users;
  }

  public void setUsers(List<DataDTO.User> users) {
    this.users = users;
  }

  @Override
  public RequestDTO.DTO_SECTION getDTOSection() {
    return RequestDTO.DTO_SECTION.LIST;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ListSuccess success)) return false;
    return Objects.equals(users, success.users);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(users);
  }
}