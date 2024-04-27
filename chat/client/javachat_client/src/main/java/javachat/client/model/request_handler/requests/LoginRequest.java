package javachat.client.model.request_handler.requests;

import javachat.client.exception.IOClientException;
import javachat.client.facade.ChatSessionController;
import javachat.client.model.ChatSessionExecutor;
import javachat.client.model.Connection;
import javachat.client.model.request_handler.IOHandler;
import javachat.client.model.request_handler.RequestInterface;
import javachat.client.model.request_handler.Requests;
import org.w3c.dom.Document;

import java.util.concurrent.locks.ReentrantLock;

public class LoginRequest implements RequestInterface {
  @Override
  public void perform(Connection connection, ChatSessionExecutor chatSession, IOHandler handler, Document message) {
    ChatSessionController controller = chatSession.getController();
    final ReentrantLock loginlock = chatSession.getLoginLock();
    if (loginlock.tryLock()) {
      try {
        String logRequest = Requests.Login(controller.getLogin(), controller.getPassword());
        synchronized (connection) {
          handler.sendMessage(connection, logRequest);
          var recMessage = handler.receiveMessage(connection);

        }
      } catch (IOClientException e) {
//        todo make ex handle
        throw new RuntimeException(e);
      } finally {
        loginlock.unlock();
      }
    }
  }
}
