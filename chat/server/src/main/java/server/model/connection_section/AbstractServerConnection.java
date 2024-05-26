package server.model.connection_section;

import io_processing.AbstractIOProcessor;
import org.slf4j.Logger;
import server.model.Server;

public interface AbstractServerConnection extends AbstractIOProcessor {
  boolean isExpired();

  void markAsExpired();

  void sendBroadcastMessage(Server server, byte[] msg, Logger log);
}
