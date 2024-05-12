package javachat.client.model.main_context;

import javachat.client.model.chat_modules.command.ChatModule;
import javachat.client.model.dto.RequestDTO;
import org.slf4j.Logger;

import java.util.concurrent.BlockingQueue;

public interface ChatSessionExecutorInterface {
  Logger getModuleLogger();

  Logger getDefaultLogger();

  BlockingQueue<RequestDTO> getModuleExchanger();

  ChatModule getChatModule();
}