package client;

import client.facade.ChatSessionController;
import client.model.main_context.ChatSessionExecutor;
import client.view.ChatSessionView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication extends javafx.application.Application {

  @Override
  public void start(Stage primaryStage) throws IOException {
    ChatSessionController chatSessionController = new ChatSessionController();
    ChatSessionView chatSessionView = new ChatSessionView(primaryStage);
    ChatSessionExecutor chatSessionExecutor = new ChatSessionExecutor(chatSessionController);

    chatSessionController.setChatSessionExecutorDependence(chatSessionExecutor);
    chatSessionController.setChatSessionViewDependence(chatSessionView);
    chatSessionView.setChatSessionController(chatSessionController);

    Scene scene = new Scene(chatSessionView);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}