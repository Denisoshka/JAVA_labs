package client.model.io_processing;

import client.model.chat_modules.ChatModuleManager;
import client.model.main_context.ChatSessionExecutor;
import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.interfaces.DTOConverterManagerInterface;
import dto.interfaces.DTOInterfaces;
import io_processing.IOProcessor;
import org.slf4j.Logger;
import org.w3c.dom.Document;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;


public class Connection implements Runnable, AutoCloseable {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(Connection.class);
  private final Socket socket;
  private volatile boolean expired;
  private final IOProcessor ioProcessor;
  private dto.DTOConverterManager dtoConverterManager;
  private ChatModuleManager chatModuleManager;
  private BlockingQueue<Document> moduleExchanger;

  public Connection(ChatSessionExecutor chatSessionExecutor, String ipaddr, int port) throws IOException {
    this.socket = new Socket(ipaddr, port);
    this.ioProcessor = new IOProcessor(socket, 0);
    dtoConverterManager = chatSessionExecutor.getDTOConverterManager();
    chatModuleManager = chatSessionExecutor.getChatModuleManager();
    moduleExchanger = chatSessionExecutor.getModuleExchanger();
  }

  @Override
  public void run() {
    try {
      while (!socket.isClosed()
              && !Thread.currentThread().isInterrupted()) {
        try {
          byte[] msg = ioProcessor.receiveMessage();
          if (msg == null) {
            continue;
          }
          var tree = dtoConverterManager.getXMLTree(msg);
          final RequestDTO.DTO_TYPE type = DTOConverterManagerInterface.getDTOType(tree);
          if (type == null) {
            continue;
          }
          log.info("message with type {}", type);

          if (type == RequestDTO.DTO_TYPE.EVENT) {
            String strsection = DTOConverterManagerInterface.getSTRDTOEvent(tree);
            RequestDTO.EVENT_TYPE section = DTOConverterManagerInterface.getDTOEvent(tree);
            if (section == null) {
              log.info("unsupported event {}", strsection);
              continue;
            }
            log.info("perform event {}", strsection);
            DTOInterfaces.EVENT_DTO event = (DTOInterfaces.EVENT_DTO) dtoConverterManager.deserialize(tree);
            chatModuleManager.getChatModule(section.geDTOSection()).eventAction(event);
          } else if (type == RequestDTO.DTO_TYPE.SUCCESS || type == RequestDTO.DTO_TYPE.ERROR) {
            log.info("response {}", type);
            moduleExchanger.put(tree);
          }
        } catch (UnableToDeserialize e) {
          log.warn(e.getMessage(), e);
        }
      }
    } catch (IOException e) {
      log.error(e.getMessage(), e);
//    todo need to make ex handle
    } catch (Exception e) {
      log.warn(e.getMessage(), e);
    } finally {
      try {
        close();
      } catch (IOException _) {
      }
    }
  }

  public boolean isClosed() {
    return socket.isClosed();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Connection that = (Connection) o;
    return Objects.equals(socket, that.socket);
  }

  @Override
  public int hashCode() {
    return Objects.hash(socket);
  }

  @Override
  public void close() throws IOException {
    log.info("close connection");
    try {
      ioProcessor.close();
    } catch (IOException _) {
    }
  }

  public void markAsExpired() {
    expired = true;
  }

  public boolean isExpired() {
    return expired;
  }

  public IOProcessor getIoProcessor() {
    return ioProcessor;
  }
}

