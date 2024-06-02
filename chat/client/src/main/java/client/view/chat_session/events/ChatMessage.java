package client.view.chat_session.events;

import client.view.chat_session.ChatSession;
import client.view.chat_session.interfaces.ChatRecord;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
    var recordMessage = new Text(messageText);
    var recordDate = new Text(zdt.format(DateTimeFormatter.ofPattern("hh : mm a ")));
    var messageFlow = new TextFlow(recordMessage);
    super(recordAgent, messageFlow, recordDate);
    this.type = type;
  }

  public ChatMessage(ChatSession.ChatEventType type, Image avatar, String recordSender,
                     String messageText, ZonedDateTime zdt) {
    var image = new ImageView(avatar);
    image.setFitHeight(40);
    image.setFitWidth(40);
    var recordAgent = new HBox(image, new Text(recordSender));
    var recordMessage = new Text(messageText);
    var recordDate = new Text(zdt.format(DateTimeFormatter.ofPattern("hh : mm a ")));
    var messageFlow = new TextFlow(recordMessage);
    super(recordAgent, messageFlow, recordDate);
    this.type = type;
  }

  @Override
  public ChatSession.ChatEventType getType() {
    return type;
  }
}
