package javachat.server.server_model;

public interface ServerMSG {
  static String getError(String desc) {
    return "<error><message>" + desc + "</message></error>";
  }

  static String getSuccess() {
    return "<success></success>";
  }

  static String getUserLogin(String desc) {
    return "<event name=\"userlogin\"><name>" + desc + "</name></event>";
  }

  static String getUserLogout(String desc) {
    return "<event name=\"userlogout\"><name>" + desc + "</name></event>";
  }

  static String getMessage(String chat, String desc) {
    return "<event name=\"message\"><from>" + chat + "</from><message>" + desc + "</message></event>";
  }
}
