package javachat.server.server_model;

public interface GetMessage {
  static String ServerErrorAnswer(String desc) {
    return "<error><message>" + desc + "</message></error>";
  }

  static String ServerSuccessAnswer() {
    return "<success></success>";
  }

  static String UnsupportedType(String desc) {
    return "<error><message>Unknown type: " + desc + "</message></error>";
  }
  static String UnsupportedCommand(String desc) {
    return "<error><message>Unknown command: " + desc + "</message></error>";
  }
}
