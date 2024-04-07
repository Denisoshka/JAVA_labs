package javachat.server.server_model.message_handler;

public enum CommandTag {
  UNSUPPORTED_COMMAND("Unsupported command"),
  LIST_COMMAND("List command"),
  MESSAGE_COMMAND("Message command"),
  LOGOUT_COMMAND("Logout command");


  final String desc;

  CommandTag(String description) {
    this.desc = description;
  }
}
