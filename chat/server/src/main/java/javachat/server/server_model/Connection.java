package javachat.server.server_model;

import javachat.server.exceptions.UnableToCreateConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class Connection implements AutoCloseable, Runnable, MessageSendInterface, MessageReceiveInterface {
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
      throw new UnableToCreateConnection("", e);
    } finally {
      try{
        if (outStream != null) outStream.close();
      }catch (IOException ignored){}
      try{
        if (inStream != null) inStream.close();
      }catch (IOException ignored){}
      try{
        if (socket != null) socket.close();
      }catch (IOException ignored){}
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
    Throwable ex = null;
    try {
      socket.close();
    } catch (IOException e) {
      ex = e;
    } finally {
      try {
        if (outStream != null) outStream.close();
      } catch (IOException e) {
        if (ex != null) e.addSuppressed(ex);
        else ex = e;
      }
      try {
        if (inStream != null) inStream.close();
      } catch (IOException e) {
        if (ex != null) e.addSuppressed(ex);
        throw e;
      }
    }
  }
  @Override
  public void receiveMessage(String message) throws IOException {
    sendMessage(message, outStream);
  }
}