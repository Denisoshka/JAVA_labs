package javachat.client.model.IOProcessing;

import java.io.IOException;

public interface IOProcessor {
  byte[] receiveMessage() throws IOException;

  void sendMessage(String xml) throws IOException;
}
