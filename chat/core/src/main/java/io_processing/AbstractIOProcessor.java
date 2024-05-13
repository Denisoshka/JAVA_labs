package io_processing;

import java.io.IOException;

public interface AbstractIOProcessor {
  byte[] receiveMessage() throws IOException;

  void sendMessage(byte[] message) throws IOException;

  boolean isClosed();
}
