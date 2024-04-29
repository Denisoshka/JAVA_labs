package javachat.client.model;

import javachat.client.facade.ChatSessionController;
import javachat.client.model.request_handler.IOHandler;
import javachat.client.model.request_handler.RequestFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ChatSessionExecutor {
  private final RequestFactory reqFactory;
  private final ExecutorService CommandContextExecutor = Executors.newSingleThreadExecutor();
  private final ExecutorService EventsContextExecutor = Executors.newSingleThreadExecutor();

  private final ChatSessionController controller;
  private final IOHandler handler;

  private Connection connection;
  private final ReentrantLock logoutLock = new ReentrantLock();
  private final ReentrantLock loginLock = new ReentrantLock();

  public ChatSessionExecutor(RequestFactory reqFactory, ChatSessionController controller, IOHandler handler) {
    this.reqFactory = reqFactory;
    this.controller = controller;
    this.handler = handler;
  }

}
