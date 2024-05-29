package server.model.server_sections;

import dto.RequestDTO;
import dto.subtypes.list.ListDTOConverter;
import dto.subtypes.logout.LogoutDTOConverter;
import dto.subtypes.message.MessageDTOConverter;
import server.model.Server;
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
            new ListSection((ListDTOConverter) manager.getConverterBySection(RequestDTO.DTO_SECTION.LIST), server)
    );
    commands.put(
            RequestDTO.DTO_SECTION.MESSAGE,
            new MessageSection((MessageDTOConverter) manager.getConverterBySection(RequestDTO.DTO_SECTION.MESSAGE), server)
    );
    commands.put(
            RequestDTO.DTO_SECTION.LOGOUT,
            new LogoutSection((LogoutDTOConverter) manager.getConverterBySection(RequestDTO.DTO_SECTION.LOGOUT), server)
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
