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
  public void perform(Connection connection, ChatSessionExecutor chatSession, IOHandler handler, String message) {

  }
}
