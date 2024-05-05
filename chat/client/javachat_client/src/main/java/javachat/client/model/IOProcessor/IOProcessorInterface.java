package javachat.client.model.IOProcessor;

import java.io.IOException;

public interface IOProcessorInterface {
  byte[] receiveMessage() throws IOException;

  void sendMessage(String xml) throws IOException;
}
