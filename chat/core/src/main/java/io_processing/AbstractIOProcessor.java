package io_processing;

import java.io.IOException;
import java.net.SocketException;

public interface AbstractIOProcessor {
  byte[] receiveMessage() throws IOException;

  void sendMessage(byte[] message) throws IOException;

  boolean isClosed();

  public void setSoTimeout(int timeout) throws SocketException;
}
