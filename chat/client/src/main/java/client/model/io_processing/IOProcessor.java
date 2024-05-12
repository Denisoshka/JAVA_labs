package client.model.io_processing;

import java.io.IOException;

public interface IOProcessor {
  byte[] receiveMessage() throws IOException;

  void sendMessage(String xml) throws IOException;
}
