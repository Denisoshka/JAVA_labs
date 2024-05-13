package server.model.server_sections.interfaces;

import dto.RequestDTO;

public interface CommandSupplier {
  /**
   * return {@code AbstractSection} which corresponds {@code RequestDTO.DTO_TYPE} if exists, else {@code null}
   */
  AbstractSection getSection(RequestDTO.DTO_SECTION section);
}
