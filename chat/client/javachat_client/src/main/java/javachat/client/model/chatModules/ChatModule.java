package javachat.client.model.chatModules;

import javachat.client.model.DTO.RequestDTO;

import java.util.List;

public interface ChatModule {
  public void perform(RequestDTO.BaseCommand command, List<Object> args);
}
