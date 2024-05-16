package client.view;

import client.facade.ChatSessionController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class ChatUsersInfo extends VBox implements ControllerIntroduce {
  @FXML
  private ListView<ChatSessionController.UserInfo> connectedUsers;
  @FXML
  private Button usersReloadButton;
  private final ObservableList<ChatSessionController.UserInfo> users = FXCollections.observableArrayList();

  private ChatSessionController chatSessionController;

  public ChatUsersInfo() {
    FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("ChatUsersInfo.fxml"));
    fxmlloader.setRoot(this);
    fxmlloader.setController(this);
    try {
      fxmlloader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
    initialize();
  }

  @FXML
  private void initialize() {
    connectedUsers.setItems(users);
    usersReloadButton.setOnAction(this::reloadUsers);

    connectedUsers.setCellFactory(param -> new ListCell<>() {
      @Override
      protected void updateItem(ChatSessionController.UserInfo user, boolean empty) {
        super.updateItem(user, empty);
        if (empty || user == null) {
          setText(null);
        } else {
          setText(user.getName());
        }
      }
    });
  }

  private void reloadUsers(Event event) {
    chatSessionController.reloadUsers();
  }

  public void onReloadUsers(List<ChatSessionController.UserInfo> usersInfo) {
    Platform.runLater(() -> {
      connectedUsers.getItems().clear();
      connectedUsers.getItems().addAll(usersInfo);
    });
  }

  @Override
  public void setChatSessionController(ChatSessionController chatSessionController) {
    this.chatSessionController = chatSessionController;
  }

  public void removeUser(ChatSessionController.UserInfo userInfo) {
    Platform.runLater(() -> {
      users.remove(userInfo);
    });
  }

  public void addUser(ChatSessionController.UserInfo userInfo) {
    Platform.runLater(() -> {
      users.add(userInfo);
    });
  }
}
