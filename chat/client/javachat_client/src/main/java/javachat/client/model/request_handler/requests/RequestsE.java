package javachat.client.model.request_handler.requests;

public enum RequestsE {
  LOGIN("login"),
  LOGOUT("logout"),
  ;


  RequestsE(String description) {
    this.description = description;
  }

  final String description;

  public String getDescription() {
    return description;
  }
}
