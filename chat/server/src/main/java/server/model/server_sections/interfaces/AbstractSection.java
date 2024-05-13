package server.model.server_sections;

import dto.RequestDTO;
import server.exceptions.IOServerException;
import server.model.io_processing.ServerConnection;

public interface AbstractSection {
  void perform(ServerConnection connection, RequestDTO dto) throws IOServerException;
}
