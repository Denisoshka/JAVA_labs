package javachat.server.server_model.interfaces;

import java.io.IOException;

public interface MessageReceiveInterface {
  void receiveMessage(String message) throws IOException;

//  void receiveServerSideMessage(String message) throws IOException;
}
