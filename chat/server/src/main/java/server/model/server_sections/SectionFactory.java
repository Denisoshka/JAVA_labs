package server.model.server_sections;

import dto.RequestDTO;
import dto.subtypes.ListDTO;
import dto.subtypes.LogoutDTO;
import dto.subtypes.MessageDTO;
import server.model.Server;
import server.model.server_sections.file.FileSection;
import server.model.server_sections.interfaces.AbstractSection;
import server.model.server_sections.interfaces.CommandSupplier;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SectionFactory implements CommandSupplier {
  private final Map<RequestDTO.DTO_SECTION, AbstractSection> commands;

  public SectionFactory(Server server) throws IOException {
    var manager = server.getConverterManager();
    commands = new HashMap<>(3);
    commands.put(
            RequestDTO.DTO_SECTION.LIST,
            new ListSection((ListDTO.ListDTOConverter) manager.getConverterBySection(RequestDTO.DTO_SECTION.LIST), server)
    );
    commands.put(
            RequestDTO.DTO_SECTION.MESSAGE,
            new MessageSection((MessageDTO.MessageDTOConverter) manager.getConverterBySection(RequestDTO.DTO_SECTION.MESSAGE), server)
    );
    commands.put(
            RequestDTO.DTO_SECTION.LOGOUT,
            new LogoutSection((LogoutDTO.LogoutDTOConverter) manager.getConverterBySection(RequestDTO.DTO_SECTION.LOGOUT), server)
    );
    commands.put(
            RequestDTO.DTO_SECTION.FILE,
            new FileSection(server)
    );
    commands.put(
            RequestDTO.DTO_SECTION.USERPROFILE,
            new UserProfileSection(server)
    );
  }

  @Override
  public AbstractSection getSection(RequestDTO.DTO_SECTION section) {
    return commands.get(section);
  }
}
