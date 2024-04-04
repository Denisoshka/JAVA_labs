package javachar.server.server_model;

public interface GetMessage {
  default String getServerErrorAnswer(String reason) {
    return reason;
  }


}
