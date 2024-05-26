package server.model.connection_section;

import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.interfaces.DTOConverterManagerInterface;
import dto.subtypes.MessageDTO;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import server.model.Server;
import server.model.server_sections.interfaces.CommandSupplier;

import java.net.SocketTimeoutException;

public class ServerWorker implements Runnable {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(ServerWorker.class);
  private final ServerConnection connection;
  private final dto.DTOConverterManager DTOConverterManager;
  private final CommandSupplier commandSupplier;
  private final Server server;

  public ServerWorker(ServerConnection connection, Server server) {
    this.server = server;
    this.connection = connection;
    this.commandSupplier = server.getCommandSupplier();
    this.DTOConverterManager = server.getConverterManager();
  }

  @Override
  public void run() {
    try (ServerConnection con = connection) {
      log.info(STR."serwer worker: \{con.getConnectionName()} started");
      while (!connection.isClosed() && !Thread.currentThread().isInterrupted()) {
        try {
          byte[] msg = con.receiveMessage();
          log.info(STR."new message from\{con.getConnectionName()}");
          final Document xmlTree = DTOConverterManager.getXMLTree(msg);
          final RequestDTO.DTO_TYPE dtoType = DTOConverterManagerInterface.getDTOType(xmlTree);
          RequestDTO.DTO_SECTION dtoSection = null;
          if (dtoType == RequestDTO.DTO_TYPE.COMMAND) {
            RequestDTO.COMMAND_TYPE commandType = DTOConverterManagerInterface.getDTOCommand(xmlTree);
            if (commandType != null) {
              dtoSection = commandType.geDTOSection();
              log.info(STR."new message for section: \{dtoSection} with type: \{dtoType}");
              commandSupplier.getSection(dtoSection).perform(connection, xmlTree, dtoType, dtoSection);
            } else {
              log.info(STR."Unrecognized command type\{DTOConverterManagerInterface.getSTRDTOCommand(xmlTree)}");
            }
          } else {
            connection.sendMessage(DTOConverterManager.serialize(
                    new MessageDTO.Error(STR."unhandled message in server protocol <\{DTOConverterManagerInterface.getSTRDTOType(xmlTree)} />")
            ).getBytes());
          }
        } catch (UnableToDeserialize e) {
          log.info(e.getMessage(), e);
        } catch (SocketTimeoutException _) {
          /*todo handle ex*/
        }
      }
    } catch (Exception e) {
      log.warn(e.getMessage(), e);
    } finally {
      log.info(STR."server worker: \{connection.getConnectionName()} finish session");
      server.submitExpiredConnection(connection);
    }
  }
}
