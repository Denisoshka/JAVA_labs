package javachat.client.model.chatModules;

import javachat.client.model.DTO.RequestDTO;
import javachat.client.model.DTO.XyiDTO;
import javachat.client.model.main_context.ChatSessionExecutor;
import javachat.client.model.main_context.Connection;
import javachat.client.model.main_context.ConnectionIntroducer;

import java.io.IOException;
import java.util.List;

public class LoginModule implements ChatModule {
  private final ConnectionIntroducer chatSessionExecutor;
  private XyiDTO.LoginData loginData;

  LoginModule(ChatSessionExecutor chatSessionExecutor) {
    this.chatSessionExecutor = chatSessionExecutor;
  }

  @Override
  public void perform(RequestDTO.BaseCommand command, List<Object> args) {
    XyiDTO.LoginData data = (XyiDTO.LoginData) args.getFirst();
    String hostname = data.getHostname();
    int port = data.getPort();
    if (loginData == null || !loginData.getHostname().equals(hostname) ||
            loginData.getPort() != port) {
      try{
        chatSessionExecutor.setConnection(new Connection(hostname, port));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

  }
}
