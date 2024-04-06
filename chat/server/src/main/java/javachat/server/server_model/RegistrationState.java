package javachat.server.server_model;

public enum RegistrationState {
  SUCCESS("success"),
  TIMEOUT_EXPIRED("timeout expired"),
  INCORRECT_NAME_DATA("incorrect name data"),
  INCORRECT_PASSWORD_DATA("incorrect password data"),
  INCORRECT_PASSWORD("incorrect password"),
  INCORRECT_LOGIN_REQUEST("incorrect login request");

  final private String description;

  RegistrationState(String desc) {
    this.description = desc;
  }

  public String getDescription() {
    return description;
  }
}
