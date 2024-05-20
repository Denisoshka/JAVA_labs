package client.model.chat_modules.submodules;

import client.model.chat_modules.interfaces.ChatModule;
import client.model.main_context.ChatSessionExecutor;
import dto.RequestDTO;
import org.w3c.dom.Document;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class FileModule implements ChatModule {
  private final BlockingQueue<Document> moduleExchanger;
  private final ExecutorService executor;

  public FileModule(ChatSessionExecutor chatSessionExecutor) {
    moduleExchanger = chatSessionExecutor.getModuleExchanger();
    executor = chatSessionExecutor.getChatModuleExecutor();
  }


  @Override
  public void commandAction(RequestDTO.BaseCommand command, List<Object> args) {
    executor.execute(() -> {

    });
  }

  @Override
  public void responseActon(RequestDTO.BaseCommand command) {

  }

  @Override
  public void eventAction(RequestDTO.BaseEvent event) {

  }
}
