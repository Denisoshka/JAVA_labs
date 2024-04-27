package javachat.client.model;

import javachat.client.facade.ChatSessionController;
import javachat.client.model.request_handler.IOHandler;
import javachat.client.model.request_handler.RequestFactory;
import javachat.client.model.request_handler.RequestInterface;
import javachat.client.model.request_handler.requests.RequestsE;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ChatSessionExecutor implements Executor {
  private final RequestFactory reqFactory;
  private final ExecutorService chatContextExecutor = Executors.newSingleThreadExecutor();

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

  public RequestInterface getRequest(String request) {
    return reqFactory.getRequest(request);
  }

  public void performLogout() {

  }

  @Override
  public void execute(@NotNull Runnable command) {
    chatContextExecutor.execute(command);
  }

  public void performLogin(String ipaddr, String port, String login, String password) throws IOException {
    this.connection = new Connection(ipaddr, port, this, handler);
    chatContextExecutor.execute(() -> reqFactory.getRequest(RequestsE.LOGIN).perform(connection, this, handler, ));
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public void deleteConnection() {
    try {
      connection.close();
    } catch (IOException ignored) {
    } finally {
      connection = null;
    }
  }

  public ChatSessionController getController() {
    return controller;
  }

  public ReentrantLock getLogoutLock() {
    return logoutLock;
  }

  public ReentrantLock getLoginLock() {
    return loginLock;
  }


/*
  @Override
  public void shutdown() {
    chatContextExecutor.shutdown();
  }

  @NotNull
  @Override
  public List<Runnable> shutdownNow() {
    return chatContextExecutor.shutdownNow();
  }

  @Override
  public boolean isShutdown() {
    return chatContextExecutor.isShutdown();
  }

  @Override
  public boolean isTerminated() {
    return chatContextExecutor.isTerminated();
  }

  @Override
  public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
    return chatContextExecutor.awaitTermination(timeout, unit);
  }

  @NotNull
  @Override
  public <T> Future<T> submit(@NotNull Callable<T> task) {
    return chatContextExecutor.submit(task);
  }

  @NotNull
  @Override
  public <T> Future<T> submit(@NotNull Runnable task, T result) {
    return chatContextExecutor.submit(task, result);
  }

  @NotNull
  @Override
  public Future<?> submit(@NotNull Runnable task) {
    return chatContextExecutor.submit(task);
  }

  @NotNull
  @Override
  public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException {
    return chatContextExecutor.invokeAll(tasks);
  }

  @NotNull
  @Override
  public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException {
    return chatContextExecutor.invokeAll(tasks, timeout, unit);
  }

  @NotNull
  @Override
  public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
    return chatContextExecutor.invokeAny(tasks);
  }

  @Override
  public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    return chatContextExecutor.invokeAny(tasks, timeout, unit);
  }
*/


}
