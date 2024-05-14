package client.model.main_context.interfaces;

import io_processing.IOProcessor;

import java.io.IOException;

public interface ConnectionModule {
  void introduceConnection(String hostname, int port) throws IOException;

  IOProcessor getIOProcessor();
}
