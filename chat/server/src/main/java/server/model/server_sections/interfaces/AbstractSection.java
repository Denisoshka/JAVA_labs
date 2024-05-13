package server.model.server_sections.interfaces;

import org.w3c.dom.Node;
import server.model.io_processing.ServerConnection;

import java.io.IOException;

public interface AbstractSection {
  void perform(ServerConnection connection, Node dto) throws IOException;
}
