package javachat.client.model;

import java.io.IOException;
import java.net.Socket;

public class ContextExecutor {
  public Socket connectChatSession(String sessionName, int port) throws IOException {
    Socket socket = new Socket(sessionName, port);
  }

  public void startChatSession() {
  }
}
