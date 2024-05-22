package client.view.chat_session.events;

import client.view.chat_session.ChatSession;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class FileEvent extends VBox implements ChatRecord {
  private final ChatSession.ChatEventType type;

  public FileEvent(ChatSession.ChatEventType type, String fileID, String from,
                   String name, long size, String mimeType, ZonedDateTime zdt) {
    Label fromLabel = new Label(STR."From: \{from}");
    Label nameLabel = new Label(STR."File: \{name}");
    Label idLabel = new Label(STR."ID: \{fileID}");
    Label sizeLabel = new Label(STR."Size: \{size}");
    Label mimeTypeLabel = new Label(STR."MIME Type: \{mimeType}");
    Label recordDate = new Label(zdt.format(DateTimeFormatter.ofPattern("hh : mm a ")));
    super(fromLabel, nameLabel, idLabel, sizeLabel, mimeTypeLabel, recordDate);
    this.type = type;
  }

  @Override
  public ChatSession.ChatEventType getType() {
    return type;
  }
}
