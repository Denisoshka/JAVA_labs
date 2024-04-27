package javachat.client.model.request_handler;

import javachat.client.model.request_handler.requests.RequestSupplier;
import javachat.client.model.request_handler.requests.RequestsE;

import java.util.HashMap;
import java.util.Map;

public class RequestFactory implements RequestSupplier {
  private Map<RequestsE, RequestInterface> events;

  public RequestFactory() {
    this.events = new HashMap<>();
//    this.events.put("list", new ListCommand());
//    this.events.put("message", new MessageCommand());
//    this.events.put("logout", new LogoutCommand());
  }


  @Override
  public RequestInterface getRequest(RequestsE command) {
    return null;
  }
}
