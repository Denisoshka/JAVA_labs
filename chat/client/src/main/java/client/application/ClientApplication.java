package client.application;

import client.facade.ChatSessionController;
import client.model.main_context.ChatSessionExecutor;
import client.view.ChatSessionView;
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
    chatSessionView.setChatSessionController(chatSessionController);

    Scene scene = new Scene(chatSessionView);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}