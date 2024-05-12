package javachat.client.view;

import javachat.client.facade.ChatSessionController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ChatSession extends HBox implements ControllerIntroduce {
  private static final int OTHERS_COLUMN_INDEX = 0;
  private static final int EVENT_COLUMN_INDEX = 1;
  private static final int USER_COLUMN_INDEX = 2;

  @FXML
  private TextField messageTextField;
  @FXML
  private Button sendButton;
  @FXML
  private ScrollPane chatScrollPane;
  @FXML
  private GridPane chatGridPane;

  private ChatSessionController chatSessionController;

  public enum ChatEventType {
    SEND,
    RECEIVE,
    EVENT,
    ;
  }

  @Override
  public void setChatSessionController(ChatSessionController chatSessionController) {
    this.chatSessionController = chatSessionController;
  }

  @FXML
  private void sendMessage() {
    chatSessionController.messageCommand(messageTextField.getText());
    messageTextField.clear();
  }

  public synchronized void addNewChatRecord(ChatRecord chatRecord) {
    ChatEventType type = chatRecord.type;
    final var columnIndex = type == ChatEventType.EVENT ? EVENT_COLUMN_INDEX :
            (type == ChatEventType.SEND ? USER_COLUMN_INDEX : OTHERS_COLUMN_INDEX);
    final var columnSpan = 2;
    final var rowIndex = chatGridPane.getRowCount();
    final var rowSpan = 1;
    chatGridPane.add(chatRecord, columnIndex, rowIndex, columnSpan, rowSpan);
  }

  public static class ChatRecord extends VBox {
    private final ChatEventType type;

    public ChatRecord(ChatEventType type, String recordSender, String messageText, ZonedDateTime zdt) {
      var recordAgent = new Text(recordSender);
      var recordDate = new Text(zdt.format(DateTimeFormatter.ofPattern("hh : mm a ")));
      var recordMessage = new Text(messageText);
      var messageFlow = new TextFlow(recordMessage);
      super(recordAgent, messageFlow, recordDate);

      this.type = type;
      if (type == ChatEventType.SEND) {
        recordMessage.getStyleClass().add("sentMessageContent");
        messageFlow.getStyleClass().addAll("sentMessage", "message");
      } else if (type == ChatEventType.RECEIVE) {
        recordMessage.getStyleClass().add("receivedMessageContent");
        messageFlow.getStyleClass().addAll("receivedMessage", "message");
      }
    }

    public ChatEventType getType() {
      return type;
    }
  }
}
