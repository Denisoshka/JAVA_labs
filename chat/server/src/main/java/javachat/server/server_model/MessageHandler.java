package javachat.server.server_model;

import javachat.server.exceptions.MessageHandlerCreateException;
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class MessageHandler {
  private final static String MAIN_TAG = "command";
  private final static String MAIN_ATTRIBUTE = "name";

  interface Command {
    void perform(Connection connection, Document inDoc) throws Exception;
  }

  private final Command logoutUser = new Command() {
    @Override
    public void perform(Connection connection, Document inDoc) throws Exception {
      server.tearConnection(connection);
    }
  };

  private final Command loginUser = new Command() {
    @Override
    public void perform(Connection connection, Document inDoc) throws Exception {

    }
  };

  private final Command listUsers = new Command() {
    @Override
    public void perform(Connection connection, Document inDoc) throws Exception {
      final var connections = server.getConnections();
      Document doc = builder.newDocument();
      Element rootElement = doc.createElement("success");
      Element usersElement = doc.createElement("users");
      doc.appendChild(rootElement);
      rootElement.appendChild(usersElement);
      for (Connection user : connections) {
        Element userElement = doc.createElement("user");
        usersElement.appendChild(userElement);
        Element nameElement = doc.createElement("name");
        nameElement.appendChild(doc.createTextNode(user.toString()));
        userElement.appendChild(nameElement);
      }
      try {
        String res = DocumtnttoString(doc);
        connection.receiveMessage(res);
      } catch (TransformerException | IOException e) {
//          todo
        throw new RuntimeException(e);
      }
    }
  };

  private final Command UnsupportedType = new Command() {
    @Override
    public void perform(Connection connection, Document inDoc) throws Exception {
      connection.receiveMessage(GetMessage.UnsupportedType(inDoc.getDocumentElement().getNodeValue()));
    }
  };

  private final Command UnsupportedCommand = new Command() {
    @Override
    public void perform(Connection connection, Document inDoc) throws Exception {
      Element element = inDoc.getDocumentElement();
      connection.receiveMessage(GetMessage.UnsupportedCommand(element.getAttribute(MAIN_ATTRIBUTE)));
    }
  };

  private final Command ReceiveAndSendMessage = new Command() {
    @Override
    public void perform(Connection connection, Document inDoc) throws Exception {
    }
  };

  private final DocumentBuilderFactory factory;
  private final DocumentBuilder builder;
  private final TransformerFactory transformerFactory;
  private final Transformer transformer;
  private final StringWriter writer;
  private final AbstractServer server;
  private final Map<String, Command> handlers;

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
    handlers.put("login", loginUser);
    handlers.put("logout", logoutUser);
    handlers.put("list", listUsers);
  }


  private String DocumtnttoString(Document doc) throws TransformerException {
    transformer.transform(new DOMSource(doc), new StreamResult(writer));
    return writer.toString();
  }

  public Command handleMessage(Connection connection, byte[] message, int len) throws IOException, SAXException {
    Document document = builder.parse(new ByteArrayInputStream(message));
    Element element = document.getDocumentElement();
    if (element.getNodeValue().compareTo(MAIN_ATTRIBUTE) == 0) {
      return handlers.get(element.getAttribute(MAIN_ATTRIBUTE));
    }
    return UnsupportedType;
  }


  String getCommandName(Element element) {
    String atr = element.getAttribute(MAIN_ATTRIBUTE);
    return atr.isEmpty() ? null : atr;
  }
}

