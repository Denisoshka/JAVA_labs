package dto.subtypes.other;

import dto.interfaces.DTOInterfaces;

public class LoginData implements DTOInterfaces.NAME, DTOInterfaces.HOSTNAME,
        DTOInterfaces.PASSWORD, DTOInterfaces.PORT {
  String name;
  String hostname;
  String password;
  int port;

  public LoginData(String name, String hostname, String password, int port) {
    this.name = name;
    this.hostname = hostname;
    this.password = password;
    this.port = port;
  }

  public String getName() {
    return this.name;
  }

  public String getHostname() {
    return this.hostname;
  }

  public String getPassword() {
    return this.password;
  }

  public int getPort() {
    return this.port;
  }
}
