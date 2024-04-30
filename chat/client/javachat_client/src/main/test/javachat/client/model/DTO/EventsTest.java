package javachat.client.model.DTO;

import javachat.client.model.DTO.events.EVENT_SECTION;
import javachat.client.model.DTO.events.MessageEvent;
import javachat.client.model.DTO.events.UserLoginEvent;
import javachat.client.model.DTO.events.UserLogoutEvent;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class EventsTest {
  static String MessageEventSTR = "<event name=\"message\"><from>CHAT_NAME_FROM</from><message>MESSAGE</message></event>";
  static String LoginEventSTR = "<event name=\"userlogin\"><name>USER_NAME</name></event>";
  static String LogoutEventSTR = "<event name=\"userlogout\"><name>USER_NAME</name></event>";


  @Test
  public void testMessageSection() throws JAXBException {
    JAXBContext eventContext = JAXBContext.newInstance(MessageEvent.class);
    Marshaller eventMarshaller = eventContext.createMarshaller();
    Unmarshaller eventUnmarshaller = eventContext.createUnmarshaller();
    eventMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

    MessageEvent createdMessageEvent = new MessageEvent("CHAT_NAME_FROM", "MESSAGE");
    StringWriter writer1 = new StringWriter();
    eventMarshaller.marshal(createdMessageEvent, writer1);
    String xmlString1 = writer1.toString();
    Assert.assertEquals(xmlString1, MessageEventSTR);
    var unmarshalledMessageEvent = (MessageEvent) eventUnmarshaller.unmarshal(new StringReader(xmlString1));
    Assert.assertEquals( unmarshalledMessageEvent, createdMessageEvent);
  }

  @Test
  public void testLoginSection() throws JAXBException {
    System.out.println(EVENT_SECTION.Event.class);
    JAXBContext eventContext = JAXBContext.newInstance(EVENT_SECTION.Event.class);
    Marshaller eventMarshaller = eventContext.createMarshaller();
    Unmarshaller eventUnmarshaller = eventContext.createUnmarshaller();
    eventMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

    UserLoginEvent createdLoginEvent = new UserLoginEvent("USER_NAME");
    StringWriter writer2 = new StringWriter();
    eventMarshaller.marshal(createdLoginEvent, writer2);
    String xmlString2 = writer2.toString();
    Assert.assertEquals(xmlString2, LoginEventSTR);
    var unmarshalledLoginEvent = eventUnmarshaller.unmarshal(new StringReader(xmlString2));
    Assert.assertEquals(unmarshalledLoginEvent, createdLoginEvent);
  }

  @Test
  public void testLogoutSection() throws JAXBException {
    System.out.println(EVENT_SECTION.Event.class);
    JAXBContext eventContext = JAXBContext.newInstance(EVENT_SECTION.Event.class);
    Marshaller eventMarshaller = eventContext.createMarshaller();
    Unmarshaller eventUnmarshaller = eventContext.createUnmarshaller();
    eventMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

    UserLogoutEvent createdLogoutEvent = new UserLogoutEvent("USER_NAME");
    StringWriter writer3 = new StringWriter();
    eventMarshaller.marshal(createdLogoutEvent, writer3);
    String xmlString3 = writer3.toString();
    Assert.assertEquals(xmlString3, LogoutEventSTR);
    var unmarshalledLogoutEvent = (UserLogoutEvent) eventUnmarshaller.unmarshal(new StringReader(xmlString3));
    Assert.assertEquals(unmarshalledLogoutEvent, createdLogoutEvent);
  }
}
