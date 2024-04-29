package javachat.client.model.request_handler;

import javachat.client.exception.IOClientException;
import javachat.client.exception.UnableToDecodeMessage;
import javachat.client.model.ChatSessionExecutor;
import javachat.client.model.Connection;
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
import java.util.concurrent.locks.ReentrantLock;

public class IOHandler {
  public final static String COMMAND_TAG = "command";
  public final static String COMMAND_ATTRIBUTE = "name";
  public final static String SUCCESS_TAG = "success";
  public final static String COMMAND_MESSAGE_TAG = "message";
  public final static String NAME_TAG = "name";
  public final static String USERS_LIST_ROOT = "users";
  public final static String USER_TAG = "user";
  private final static String PASSWORD_TAG = "password";
  private final static String LOGIN_TAG = "login";

  //  private final ContextExecutor contextExecutor;
  private final RequestFactory commandFactory;
  private final StringWriter writer;
  private final ChatSessionExecutor chatSessionModel;


  public IOHandler(ChatSessionExecutor chatSessionModel) throws ParserConfigurationException, TransformerConfigurationException {
    this.chatSessionModel = chatSessionModel;
    this.writer = new StringWriter();
    this.commandFactory = new RequestFactory();
//    this.factory = DocumentBuilderFactory.newInstance();
//    this.builder = factory.newDocumentBuilder();
//    this.transformerFactory = TransformerFactory.newInstance();
//    this.transformer = transformerFactory.newTransformer();
  }

  public Document receiveMessage(Connection connection) throws IOClientException, UnableToDecodeMessage {
    return receiveMessage(connection.getReceiveStream());
  }

  public Document receiveMessage(DataInputStream connection) throws IOClientException, UnableToDecodeMessage {
    int len;
    byte[] msg;
    Document doc;
    try {
      synchronized (connection) {
        len = connection.readInt();
        msg = connection.readNBytes(len);
      }
    } catch (IOException e) {
      throw new IOClientException(e.getMessage(), e);
    }
    try (ByteArrayInputStream stream = new ByteArrayInputStream(msg, 0, len)) {
      doc = builder.parse(stream);
    } catch (SAXException | IOException e) {
      throw new UnableToDecodeMessage(e.getMessage(), e);
    }
    return doc;
  }

  public void sendMessage(Connection connection, String message) throws IOClientException {
    sendMessage(connection.getSendStream(), message);
  }

  public void sendMessage(DataOutputStream connection, String message) throws IOClientException {
    try {
      synchronized (connection) {
        connection.writeInt(message.getBytes().length);
        connection.write(message.getBytes(), 0, message.getBytes().length);
      }
    } catch (IOException e) {
      throw new IOClientException(e.getMessage(), e);
    }
  }

  public void sendMessage(Connection connection, Document message) throws IOClientException {
    String converterMSG = documentToString(message);
    sendMessage(connection, converterMSG);
  }

  public void sendMessage(DataOutputStream connection, Document message) throws IOClientException {
    String converterMSG = documentToString(message);
    sendMessage(connection, converterMSG);
  }

  public RequestInterface handleMessage(Document message) {
    Element root = message.getDocumentElement();
    String comName;
    if (root.getNodeName().compareTo(COMMAND_TAG) != 0) return null;
    if ((comName = root.getAttribute(NAME_TAG)).isEmpty()) return null;
    return commandFactory.getRequest(comName);
  }

  public String getConnectionName(Document doc) {
    return doc.getDocumentElement().getElementsByTagName(NAME_TAG).item(0).getTextContent();
  }

  public String documentToString(Document document) {
    try {
      transformer.transform(new DOMSource(document), new StreamResult(writer));
    } catch (TransformerException e) {
      throw new UnableToDecodeMessage(e.getMessage(), e);
    }
    return writer.toString();
  }

  public Document stringToDocument(String message) {
    Document doc;
    try (ByteArrayInputStream stream = new ByteArrayInputStream(message.getBytes(), 0, message.getBytes().length)) {
      return builder.parse(stream);
    } catch (SAXException | IOException e) {
      throw new UnableToDecodeMessage(e.getMessage(), e);
    }
  }


  public DocumentBuilderFactory getFactory() {
    return factory;
  }

  public DocumentBuilder getBuilder() {
    return builder;
  }

  public TransformerFactory getTransformerFactory() {
    return transformerFactory;
  }

  public Transformer getTransformer() {
    return transformer;
  }

  public StringWriter getWriter() {
    return writer;
  }
}

