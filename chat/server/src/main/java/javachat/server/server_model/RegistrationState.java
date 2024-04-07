package javachat.server.server_model;

public enum RegistrationState {
  SUCCESS("Success"),
  TIMEOUT_EXPIRED("Timeout expired"),
  INCORRECT_NAME_DATA("Incorrect name data"),
  INCORRECT_PASSWORD_DATA("Incorrect password data"),
  INCORRECT_PASSWORD("Incorrect password"),
  INCORRECT_LOGIN_REQUEST("Incorrect login request");

  final private String description;

  RegistrationState(String desc) {
    this.description = desc;
  }

  public String getDescription() {
    return description;
  }
}
