package javachat.client.model.main_context;

import javachat.client.model.IOProcessing.IOProcessor;

import java.io.IOException;

public interface ConnectionModule {
  IOProcessor introduceConnection(String hostname, int port) throws IOException;

  IOProcessor getIOProcessor();
}
