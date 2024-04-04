package javachat.server.server_model;

public interface GetMessage {
  static String getServerErrorAnswer(String desc) {
    return "<error><message>" + desc + "</message></error>";
  }

  static String ServerSuccessAnswer() {
    return "<success></success>";
  }
}
