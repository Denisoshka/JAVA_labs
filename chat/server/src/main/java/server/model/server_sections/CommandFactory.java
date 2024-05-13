package server.model.server_sections;

import dto.RequestDTO;
import dto.subtypes.ListDTO;
import dto.subtypes.LogoutDTO;
import dto.subtypes.MessageDTO;
import server.model.Server;
import server.model.server_sections.interfaces.AbstractSection;
import server.model.server_sections.interfaces.CommandSupplier;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory implements CommandSupplier {
  private final Map<RequestDTO.DTO_SECTION, AbstractSection> commands;

  public CommandFactory(Server server) {
    var manager = server.getConverterManager();
    this.commands = new HashMap<>(3);
    this.commands.put(
            RequestDTO.DTO_SECTION.LIST,
            new ListSection((ListDTO.ListDTOConverter) manager.getConverter(RequestDTO.DTO_SECTION.LIST), server)
    );
    this.commands.put(
            RequestDTO.DTO_SECTION.MESSAGE,
            new MessageSection((MessageDTO.MessageDTOConverter) manager.getConverter(RequestDTO.DTO_SECTION.MESSAGE), server)
    );
    this.commands.put(
            RequestDTO.DTO_SECTION.LOGOUT,
            new LogoutSection((LogoutDTO.LogoutDTOConverter) manager.getConverter(RequestDTO.DTO_SECTION.LOGOUT), server)
    );
  }

  @Override
  public AbstractSection getSection(RequestDTO.DTO_SECTION section) {
    return commands.get(section);
  }
}
