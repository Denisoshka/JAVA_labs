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
  public IOHandler(ChatSessionExecutor chatSessionModel) throws ParserConfigurationException, TransformerConfigurationException {
  }
}

