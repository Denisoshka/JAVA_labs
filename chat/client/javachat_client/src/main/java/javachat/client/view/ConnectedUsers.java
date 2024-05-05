package javachat.client.view;

import javachat.client.facade.ChatSessionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class ConnectedUsers extends VBox implements ControllerIntroduce {
  @FXML
  private ListView<String> connectedUsers;
  private ObservableList<String> users = FXCollections.observableArrayList();
//  ChatSessionController controller;

  public ConnectedUsers() {
    FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("ConnectedUsers.fxml"));
    fxmlloader.setRoot(this);
    fxmlloader.setController(this);
    try {
      fxmlloader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }

    connectedUsers.setItems(users);
  }

  @Override
  public void setController(ChatSessionController controller) {
//    this.controller = controller;
  }

  public void showUsers(List<String> newUsers) {
    users.addAll(newUsers);
  }

  public void removeUser(String user) {
    users.remove(user);
  }

  public void addUser(String user) {
    users.add(user);
  }
}
