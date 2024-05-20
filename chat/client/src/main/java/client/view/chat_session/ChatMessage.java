package client.view.chat_session;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage extends VBox implements ChatRecord {
  private final ChatSession.ChatEventType type;

  public ChatMessage(ChatSession.ChatEventType type, String recordSender,
                     String messageText, ZonedDateTime zdt) {
    var recordAgent = new Text(recordSender);
    var recordDate = new Text(zdt.format(DateTimeFormatter.ofPattern("hh : mm a ")));
    var recordMessage = new Text(messageText);
    var messageFlow = new TextFlow(recordMessage);
    super(recordAgent, messageFlow, recordDate);
    this.type = type;
  }

  @Override
  public ChatSession.ChatEventType getType() {
    return type;
  }
}
