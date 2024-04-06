package javachat.server.server_model;

import javachat.server.exceptions.IOServerException;
import javachat.server.exceptions.MessageHandlerCreateException;
import javachat.server.exceptions.UnableToDecodeMessage;
import javachat.server.server_model.interfaces.AbstractServer;
import javachat.server.server_model.interfaces.GetMessage;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MessageHandler {
  private final static String MAIN_TAG = "command";
  private final static String MAIN_ATTRIBUTE = "name";
  private final static String SUCCESS_TAG = "success";
  private final static String NAME_TAG = "name";
  private final static String USERS_LIST_ROOT = "users";
  private final static String USER_TAG = "user";
  private final static String PASSWORD_TAG = "password";
  private final static String LOGIN_TAG = "login";

  private final DocumentBuilderFactory factory;
  private final DocumentBuilder builder;
  private final TransformerFactory transformerFactory;
  private final Transformer transformer;
  private final StringWriter writer;
  private final AbstractServer server;
  private final Map<String, Command> handlers;

  public interface Command {
    void perform(Connection connection, Document inDoc, String desc) throws Exception;
  }

  private final Command logout = new Command() {
    @Override
    public void perform(Connection connection, Document inDoc, String desc) throws Exception {
      server.tearConnection(connection);
    }
  };

  private final Command login = new Command() {
    @Override
    public void perform(Connection connection, Document inDoc, String desc) throws Exception {

    }
  };
  private final Command userlogin = new Command() {
    @Override
    public void perform(Connection connection, Document inDoc, String desc) throws Exception {

    }
  };

  private final Command listUsers = new Command() {
    @Override
    public void perform(Connection connection, Document inDoc, String desc) throws Exception {
      final var connections = server.getConnections();
      Document doc = builder.newDocument();
      Element rootElement = doc.createElement(SUCCESS_TAG);
      Element usersElement = doc.createElement(USERS_LIST_ROOT);
      doc.appendChild(rootElement);
      rootElement.appendChild(usersElement);
      for (Connection user : connections) {
        Element userElement = doc.createElement(USER_TAG);
        usersElement.appendChild(userElement);
        Element nameElement = doc.createElement(NAME_TAG);
        nameElement.appendChild(doc.createTextNode(user.toString()));
        userElement.appendChild(nameElement);
      }
      try {
        String res = documentToString(doc);
        connection.receiveMessage(res);
      } catch (TransformerException | IOException e) {
//          todo
        throw new RuntimeException(e);
      }
    }
  };

  private final Command UnsupportedType = new Command() {
    @Override
    public void perform(Connection connection, Document inDoc, String desc) throws Exception {
      MessageHandler.this.sendMessage(GetMessage.UnsupportedType(inDoc.getDocumentElement().getNodeValue()).getBytes(), connection.getSendStream());
    }
  };

  private final Command BrokenData = new Command() {
    @Override
    public void perform(Connection connection, Document inDoc, String desc) throws Exception {
      MessageHandler.this.sendMessage(GetMessage.BrokenMessageData(desc).getBytes(), connection.getSendStream());
    }
  };

  private final Command UnsupportedCommand = new Command() {
    @Override
    public void perform(Connection connection, Document inDoc, String desc) throws Exception {
      Element element = inDoc.getDocumentElement();
      connection.receiveMessage(GetMessage.UnsupportedCommand(element.getAttribute(MAIN_ATTRIBUTE)));
    }
  };

  private final Command ReceiveAndSendMessage = new Command() {
    @Override
    public void perform(Connection connection, Document inDoc, String desc) throws Exception {
    }
  };

  public MessageHandler(AbstractServer server) throws MessageHandlerCreateException {
    this.server = server;
    try {
      this.writer = new StringWriter();
      this.factory = DocumentBuilderFactory.newInstance();
      this.builder = factory.newDocumentBuilder();
      this.transformerFactory = TransformerFactory.newInstance();
      this.transformer = transformerFactory.newTransformer();
    } catch (ParserConfigurationException | TransformerConfigurationException e) {
      throw new MessageHandlerCreateException(e);
    }
    handlers = new HashMap<>();
    handlers.put("login", login);
    handlers.put("logout", logout);
    handlers.put("list", listUsers);
  }

  private String documentToString(Document doc) throws TransformerException {
    transformer.transform(new DOMSource(doc), new StreamResult(writer));
    return writer.toString();
  }

  public Command handleMessage(Document doc) {
    if (doc == null) {
      return BrokenData;
    }
    Element element = doc.getDocumentElement();
    String atr = null;
    Command com = null;
    if (element.getNodeValue().compareTo(MAIN_TAG) == 0) {
      if (!(atr = element.getAttribute(MAIN_ATTRIBUTE)).isEmpty() || (com = handlers.get(atr)) == null)
        return UnsupportedCommand;
      return com;
    }
    return UnsupportedType;
  }


  private boolean correctLoginRequest(Element root) {
    if (root.getNodeName().compareTo(MAIN_TAG) != 0) return false;
    if (root.getAttribute(NAME_TAG).compareTo(LOGIN_TAG) != 0) return false;
    if (root.getElementsByTagName(NAME_TAG).getLength() != 1) return false;
    return root.getElementsByTagName(PASSWORD_TAG).getLength() == 1;
  }

  public Connection getNewConnection(Socket socket, DataOutputStream sendStream, DataInputStream receiveStream) throws IOServerException {
    try {
      Document document = receiveMessage(receiveStream);
      Element root = document.getDocumentElement();
      //  todo
      if (correctLoginRequest(root)) {
        return null;
      }
      String name = root.getElementsByTagName(NAME_TAG).item(0).getTextContent();
      String password = root.getElementsByTagName(PASSWORD_TAG).item(0).getTextContent();
      if (!server.registerNewConnection(name, password)) {
        return null;
      }
      Connection conn = new Connection(this, name, socket);
      sendMessage(GetMessage.ServerSuccessAnswer(), conn.getSendStream());
      broadcastSendMessage();
      server.addConnection(conn);

      return conn;

    } catch (IOServerException e) {

    }

    try {
      String errMsg = GetMessage.ServerErrorAnswer();
      sendMessage(errMsg, sendStream);
    } catch (IOException ignored) {
      return null;
    }
    return null;
  }

  public Document receiveMessage(DataInputStream receiveStream) throws IOServerException, UnableToDecodeMessage {
    int len;
    byte[] recData = null;
    Document doc = null;
    try {
      len = receiveStream.readInt();
      recData = receiveStream.readNBytes(len);
    } catch (IOException e) {
      throw new IOServerException(e.getMessage(), e);
    }
    try (ByteArrayInputStream stream = new ByteArrayInputStream(recData, 0, len)) {
      doc = builder.parse(stream);
    } catch (SAXException | IOException e) {
      throw new UnableToDecodeMessage(e.getMessage(), e);
    }
    return doc;
  }

  public void sendMessage(Document doc, @NotNull DataOutputStream sendStream) throws IOServerException {
    try {
      String msg = documentToString(doc);
      sendMessageString(msg, sendStream);
    } catch (TransformerException e) {
      throw new RuntimeException(e);
    }
  }

  private void sendMessageString(String msg, @NotNull DataOutputStream sendStream) throws IOServerException {
    try {
      synchronized (sendStream) {
        sendStream.write(msg.getBytes().length);
        sendStream.write(msg.getBytes(), 0, msg.getBytes().length);
      }
    } catch (IOException e) {
      throw new IOServerException(e.getMessage(), e);
    }
  }

  public void broadcastSendMessage(Document broadcastMsg, Connection source) throws IOServerException {
    var connections = server.getConnections();
    synchronized (connections) {
      for (var conn : connections) {
        if (conn != source) {
          try {
            sendMessage(broadcastMsg, conn.getSendStream());
          } catch (IOServerException e){

          }
        }
      }
    }

  }

  String getCommandName(Element element) {
    String atr = element.getAttribute(MAIN_ATTRIBUTE);
    return atr.isEmpty() ? null : atr;
  }
}

