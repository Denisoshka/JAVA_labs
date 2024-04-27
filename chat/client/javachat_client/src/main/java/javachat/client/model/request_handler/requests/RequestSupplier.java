package javachat.client.model.request_handler.requests;

import javachat.client.model.request_handler.RequestInterface;

public interface RequestSupplier {
  RequestInterface getRequest(RequestsE command);
}
