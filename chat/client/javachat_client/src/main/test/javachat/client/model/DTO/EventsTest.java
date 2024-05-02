package javachat.client.model.DTO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import javachat.client.model.DTO.events.Event;
import javachat.client.model.DTO.events.MessageEvent;
import javachat.client.model.DTO.events.Userlogin;
import javachat.client.model.DTO.events.Userlogout;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

public class EventsTest {
  static String MessageEventSTR = "<event name=\"message\"><from>CHAT_NAME_FROM</from><message>MESSAGE</message></event>";
  static String LoginEventSTR = "<event name=\"userlogin\"><name>USER_NAME</name></event>";
  static String LogoutEventSTR = "<event name=\"userlogout\"><name>USER_NAME</name></event>";
  static String BaseEvent = "<event name=\"base\"/>";
  static PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
          .allowIfSubType("javachat.client.model.DTO.events")
          .build();
  static XmlMapper xmlMapper = new XmlMapper();

  @BeforeAll
  static void initMapper() {
    xmlMapper.activateDefaultTyping(ptv);
    xmlMapper.getSerializationConfig().getDefaultVisibilityChecker()
            .withFieldVisibility(JsonAutoDetect.Visibility.NONE)
            .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withCreatorVisibility(JsonAutoDetect.Visibility.NONE);
//    xmlMapper.getSerializationConfig().getDefaultVisibilityChecker().withIsGetterVisibility(JsonAutoDetect.Visibility.NONE);
  }

  @Test

  public void testBaseEvent() throws JsonProcessingException {
    Event event = new Event();
    String xmlString = xmlMapper.writeValueAsString(event);
    Assert.assertEquals(xmlString, BaseEvent);
    var unmarshalledEvent = xmlMapper.readValue(xmlString, Event.class);
    Assert.assertEquals(unmarshalledEvent, event);
  }


  @Test
  public void testMessageSection() throws JsonProcessingException {
    MessageEvent createdMessageEvent = new MessageEvent("CHAT_NAME_FROM", "MESSAGE");
//    createdMessageEvent.setMessage("MESSAGE");
//    createdMessageEvent.setFrom("CHAT_NAME_FROM");

    String xmlString1 = xmlMapper.writeValueAsString(createdMessageEvent);
    Assert.assertEquals(xmlString1, MessageEventSTR);
    var unmarshalledMessageEvent = xmlMapper.readValue(xmlString1, Event.class);
    Assert.assertEquals(unmarshalledMessageEvent, createdMessageEvent);
  }

  @Test
  public void testLoginSection() throws JsonProcessingException {

    Userlogin createdLoginEvent = new Userlogin("USER_NAME");
    String xmlString2 = xmlMapper.writeValueAsString(createdLoginEvent);
    Assert.assertEquals(xmlString2, LoginEventSTR);

    var unmarshalledLoginEvent = xmlMapper.readValue(xmlString2, Event.class);
    Assert.assertEquals(unmarshalledLoginEvent, createdLoginEvent);
  }

  @Test
  public void testLogoutSection() throws JsonProcessingException {
    Userlogout createdLogoutEvent = new Userlogout("USER_NAME");
    String xmlString3 = xmlMapper.writeValueAsString(createdLogoutEvent);
    Assert.assertEquals(xmlString3, LogoutEventSTR);

    var unmarshalledLogoutEvent = xmlMapper.readValue(xmlString3, Event.class);
    Assert.assertEquals(unmarshalledLogoutEvent, createdLogoutEvent);
  }
}
