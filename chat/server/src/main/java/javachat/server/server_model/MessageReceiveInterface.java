package javachat.server.server_model;

import java.io.IOException;

public interface MessageReceiveInterface {
  void receiveMessage(String message) throws IOException;

  void receiveServerMessage(String message) throws IOException;
}
