package server.model.io_processing;

import io_processing.AbstractIOProcessor;

public interface AbstractServerConnection extends AbstractIOProcessor {
  boolean isExpired();

  void markAsExpired();
}
