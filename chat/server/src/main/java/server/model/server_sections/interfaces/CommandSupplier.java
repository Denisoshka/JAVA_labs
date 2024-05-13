package server.model.server_sections.interfaces;

import dto.RequestDTO;

public interface CommandSupplier {
  AbstractSection getSection(RequestDTO.DTO_SECTION section);
}
