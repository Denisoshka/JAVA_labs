package javachat.server.server_model;

import javachat.server.exceptions.MessageHandlerCreateException;
import org.w3c.dom.Document;
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
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Connection implements AutoCloseable, Runnable, MessageSendInterface, MessageReceiveInterface {
  public final static int BUFFERSIZE = 41_943_040;
  private final Set<Connection> connections;
  private final Socket clSocket;
  private final String connectionName;
  private DataOutputStream outStream = null;
  private DataInputStream inStream = null;

  @Override
  public String toString() {
    return connectionName;
  }

  public Connection(String connectionName, Socket clSocket, Set<Connection> connections) throws MessageHandlerCreateException {
    this.connectionName = connectionName;
    this.connections = connections;
    this.clSocket = clSocket;
  }

  @Override
  public void run() {
    try (DataOutputStream outStream = new DataOutputStream(clSocket.getOutputStream());
         DataInputStream inStream = new DataInputStream(clSocket.getInputStream());
    ) {
      while (!clSocket.isClosed()) {
        byte[] msg = receiveInput(inStream);

      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private byte[] receiveInput(DataInputStream inStream) throws IOException {
    return inStream.readNBytes(inStream.readInt());
  }

  private void writeMessage(String message, DataOutputStream outStream) throws IOException {
    int msgLen = message.getBytes().length;
    outStream.write(msgLen);
    outStream.write(message.getBytes(), 0, msgLen);
  }

  @Override
  public void close() throws Exception {
    clSocket.close();
  }

  @Override
  public void receiveMessage(String message) throws IOException {
    sendMessage(message, outStream);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Connection that = (Connection) o;
    return Objects.equals(clSocket, that.clSocket);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clSocket);
  }
}