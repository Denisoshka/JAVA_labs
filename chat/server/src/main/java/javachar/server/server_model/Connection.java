package javachar.server.server_model;

import javachar.server.exceptions.UnableToCreateConnection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class Connection implements AutoCloseable, Runnable, MessageSendInterface {

  public final static int BUFFERSIZE = 41_943_040;
  private final List<Connection> connections;
  private final Socket socket;
  private final DocumentBuilderFactory factory;
  private final DocumentBuilder builder;
  private DataOutputStream outStream = null;
  private DataInputStream inStream = null;

  public Connection(Socket clSocket, List<Connection> connections) throws UnableToCreateConnection {
    this.connections = connections;
    this.socket = clSocket;
    try {
      this.outStream = new DataOutputStream(clSocket.getOutputStream());
      this.inStream = new DataInputStream(clSocket.getInputStream());
      this.factory = DocumentBuilderFactory.newInstance();
      this.builder = factory.newDocumentBuilder();
    } catch (IOException | ParserConfigurationException e) {
      if (outStream != null) {
        try {
          outStream.close();
        } catch (IOException ignored) {
        }
      }
      if (inStream != null) {
        try {
          inStream.close();
        } catch (IOException ignored) {
        }
      }
      throw new UnableToCreateConnection("", e);
    }
  }

  @Override
  public void run() {

  }

  private void writeMessage(String message, DataOutputStream outStream) throws IOException {
    int msgLen = message.getBytes().length;
    outStream.write(msgLen);
    outStream.write(message.getBytes(), 0, msgLen);
  }

  @Override
  public void close() throws Exception {
    socket.close();
  }
}