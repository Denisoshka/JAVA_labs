package javachat.client.model.request_handler;

public interface Requests {
  static String Login(String name, String password) {
    return "<command name=\"login\">" +
            "<name>" + name + "</name>" +
            "<password>" + password + "</password>" +
            "</command>";
  }

  static String Logout(String desc) {
    return "<command name=\"logout\">" +
            "</command>";
  }

  static String Message(String message) {
    return "<command name=\"message\">" +
            "<message>" + message + "</message>" +
            "</command>";
  }

  static String getRequest(String tag, String tagAttribute, String tagAttributeValue, String tagValue) {
    return null;
  }


}