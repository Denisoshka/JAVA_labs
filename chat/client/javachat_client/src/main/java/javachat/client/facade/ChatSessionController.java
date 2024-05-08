package javachat.client.facade;

import javachat.client.model.dto.DataDTO;
import javachat.client.model.dto.subtypes.LoginDTO;
import javachat.client.model.main_context.ChatSessionExecutor;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;

import java.util.List;

@Slf4j
public class ChatSessionController {
  ChatSessionExecutor executor;

  private String login;
  private String password;

  public void login(Document loginRequest) {
//    executor.performLogin(loginRequest);
  }


  public String getLogin() {
    return login;
  }

  public String getPassword() {
    return password;
  }

  public void showUsers(List<DataDTO.User> users) {

  }

  public void addUser(LoginDTO.Event event){
    event.getName();
  }
}
