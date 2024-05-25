package server.model.connection_section;

import io_processing.AbstractIOProcessor;

public interface AbstractServerConnection extends AbstractIOProcessor {
  boolean isExpired();

  void markAsExpired();
}
