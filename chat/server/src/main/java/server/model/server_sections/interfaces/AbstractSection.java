package server.model.server_sections.interfaces;

import dto.RequestDTO;
import org.w3c.dom.Document;
import server.model.io_processing.ServerConnection;

import java.io.IOException;

public interface AbstractSection {
  void perform(ServerConnection connection, Document root, RequestDTO.DTO_TYPE type, RequestDTO.DTO_SECTION section) throws IOException;
}
