package server.model.server_sections;

import org.w3c.dom.Node;
import server.exceptions.IOServerException;
import server.model.io_processing.ServerConnection;
import server.model.server_sections.interfaces.AbstractSection;

public class LoginSection implements AbstractSection {
  @Override
  public void perform(ServerConnection connection, Node dto) throws IOServerException {
//    todo
  }
}
