package client.view.chat_session.events;

import client.facade.ChatSessionController;
import client.view.chat_session.ChatSession;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class FileEvent extends VBox implements ChatRecord {
  private final ChatSession.ChatEventType type;
  private final ChatSessionController controller;
  private final long id;

  public FileEvent(ChatSessionController controller, ChatSession.ChatEventType type, long fileID, String from,
                   String name, long size, String mimeType, ZonedDateTime zdt) {
    Label fromLabel = new Label(STR."From: \{from}");
    Label nameLabel = new Label(STR."ID: \{fileID} file: \{name}");
    Label sizeLabel = new Label(STR."Size: \{size}");
    Label mimeTypeLabel = new Label(STR."MIME Type: \{mimeType}");
    Label recordDate = new Label(zdt.format(DateTimeFormatter.ofPattern("hh : mm a ")));
    Button downloadButton = new Button("Download");
    super(fromLabel, nameLabel, sizeLabel, mimeTypeLabel, recordDate);
    super.getChildren().add(downloadButton);
    downloadButton.setOnAction(this::action);
    this.controller = controller;
    this.type = type;
    this.id = fileID;
  }

  @Override
  public ChatSession.ChatEventType getType() {
    return type;
  }

  private void action(Event event) {
    controller.downloadFile(id);
  }
}
