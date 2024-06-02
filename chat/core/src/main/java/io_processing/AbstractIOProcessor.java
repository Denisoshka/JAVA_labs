package io_processing;

import java.io.IOException;
import java.net.SocketException;

public interface AbstractIOProcessor {
  /**
   * return {@code null} if no available message in channel
   * */
  byte[] receiveMessage() throws IOException;

  void sendMessage(byte[] message) throws IOException;

  boolean isClosed();

  void setSoTimeout(int timeout) throws SocketException;
}
