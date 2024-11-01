package client.model.main_context.interfaces;

import io_processing.IOProcessor;

import java.io.IOException;

public interface ConnectionModule {
  enum ConnectionState {
    CONNECTED,
    DISCONNECTED,
  }

  void introduceConnection(String hostname, int port) throws IOException;

  void shutdownConnection() throws IOException;

  IOProcessor getIOProcessor();
}
