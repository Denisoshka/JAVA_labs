package javachat.client;

import javachat.client.facade.ChatSessionController;
import javachat.client.model.main_context.ChatSessionExecutor;
import javachat.client.view.ChatSessionView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication extends Application {

  @Override
  public void start(Stage stage) throws IOException {
    ChatSessionController chatSessionController = new ChatSessionController();
    ChatSessionView chatSessionView = new ChatSessionView();
    ChatSessionExecutor chatSessionExecutor = new ChatSessionExecutor(chatSessionController);

    chatSessionController.setChatSessionExecutorDependence(chatSessionExecutor);
    chatSessionController.setChatSessionViewDependence(chatSessionView);

    Scene scene = new Scene(chatSessionView);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}