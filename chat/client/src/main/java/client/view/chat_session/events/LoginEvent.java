package client.view.chat_session.events;

import client.view.chat_session.ChatSession;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class LoginEvent extends VBox implements ChatRecord {
  public LoginEvent(String name, ZonedDateTime zdt) {
    var recordDate = new Label(zdt.format(DateTimeFormatter.ofPattern("hh : mm a ")));
    var user = new Label(STR."user: \{name} login");
    super(user, recordDate);
  }

  @Override
  public ChatSession.ChatEventType getType() {
    return ChatSession.ChatEventType.EVENT;
  }
}
