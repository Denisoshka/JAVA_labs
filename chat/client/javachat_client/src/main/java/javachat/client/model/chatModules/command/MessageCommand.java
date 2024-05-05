package javachat.client.model.chatModules.command;

import javachat.client.model.DTO.RequestDTO;
import javachat.client.model.main_context.ChatSessionExecutor;
import org.slf4j.Logger;

import java.util.List;

public class MessageCommand implements ChatCommand {
  private final ChatSessionExecutor chatSessionExecutor;
  private final Logger modulelogger;
  private final Logger defaultLoger;

  public MessageCommand(ChatSessionExecutor chatSessionExecutor) {
    this.chatSessionExecutor = chatSessionExecutor;
    this.modulelogger = chatSessionExecutor.getModuleLogger();
    this.defaultLoger = chatSessionExecutor.getDefaultLogger();
  }

  @Override
  public void commandAction(RequestDTO.BaseCommand command, List<Object> args) throws InterruptedException {
    var ioProcessor = chatSessionExecutor.getIOProcessor();
    var converter = chatSessionExecutor.getXMLDTOConverterManager();
    chatSessionExecutor.executeAction(()->{

    });
  }

  @Override
  public void responseActon() {

    return null;
  }
}
