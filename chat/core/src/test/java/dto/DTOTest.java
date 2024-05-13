package dto;

import dto.subtypes.ListDTO;
import dto.subtypes.LoginDTO;
import dto.subtypes.LogoutDTO;
import dto.subtypes.MessageDTO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class DTOTest {
  static String ListCommandSTR = "<command name=\"list\"/>";
  static String ListSuccessSTR = "<success><users><user><name>USER_1</name></user><user><name>USER_2</name></user></users></success>";
  static String ListErrorSTR = "<error><message>XYI</message></error>";

  static String LoginEventSTR = "<event name=\"userlogin\"><name>USER_NAME</name></event>";
  static String LoginCommandSTR = "<command name=\"login\"><name>USER_NAME</name><password>PASSWORD</password></command>";
  static String LoginSuccessSTR = "<success/>";
  static String LoginErrorSTR = "<error><message>XYI</message></error>";

  static String LogoutEventSTR = "<event name=\"userlogout\"><name>USER_NAME</name></event>";
  static String LogoutCommandSTR = "<command name=\"logout\"/>";
  static String LogoutSuccessSTR = "<success/>";
  static String LogoutErrorSTR = "<error><message>XYI</message></error>";

  static String MessageEventSTR = "<event name=\"message\"><from>CHAT_NAME_FROM</from><message>MESSAGE</message></event>";
  static String MessageCommandSTR = "<command name=\"message\"><message>MESSAGE</message></command>";
  static String MessageSuccessSTR = "<success/>";
  static String MessageErrorSTR = "<error><message>XYI</message></error>";


  static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

  static ListDTO.ListAbstractDTOConverter listDTOConverter;
  static MessageDTO.MessageAbstractDTOConverter messageDTOConverter;
  static LogoutDTO.LogoutAbstractDTOConverter logoutDTOConverter;
  static LoginDTO.LoginAbstractDTOConverter loginDTOConverter;

  @Before
  public void prepare() throws ParserConfigurationException, JAXBException {
    listDTOConverter = new ListDTO.ListAbstractDTOConverter();
    loginDTOConverter = new LoginDTO.LoginAbstractDTOConverter();
    logoutDTOConverter = new LogoutDTO.LogoutAbstractDTOConverter();
    messageDTOConverter = new MessageDTO.MessageAbstractDTOConverter();
  }

  @Test
  public void ListDTOTest() throws JAXBException {
/*
   String xmlString1 = xmlMapper.writeValueAsString(createdMessageEvent);
    Assert.assertEquals(xmlString1, MessageEventSTR);
    var unmarshalledMessageEvent = xmlMapper.readValue(xmlString1, Event.class);
    Assert.assertEquals(unmarshalledMessageEvent, createdMessageEvent);

    ListDTO.Command command = new ListDTO.Command();
    String xml = listDTOConverter.serialize(command);
    Assert.assertEquals(xml, ListCommandSTR);*/
  }

  @ParameterizedTest
  @MethodSource("ArgsEventDTOTest")
  public void EventDTOTest(RequestDTO eventto, String eventXml, RequestDTO.AbstractDTOConverter converter) throws JAXBException, IOException, SAXException, ParserConfigurationException {
    DocumentBuilder builder = Objects.requireNonNull(factory.newDocumentBuilder());
    String serializableXML = converter.serialize(eventto);
    Assert.assertEquals(eventXml, serializableXML);

    RequestDTO eventfrom = converter.deserialize(
            converter.getXMLTree(builder, eventXml)
    );
    Assert.assertEquals(eventto, eventfrom);
  }

  public static Stream<Arguments> ArgsEventDTOTest() throws JAXBException {
    var messageConverter = new MessageDTO.MessageAbstractDTOConverter();
    var logoutConverter = new LogoutDTO.LogoutAbstractDTOConverter();
    var loginConverter = new LoginDTO.LoginAbstractDTOConverter();
    var listConverter = new ListDTO.ListAbstractDTOConverter();

    return Stream.of(
            Arguments.of(
                    new MessageDTO.Event("CHAT_NAME_FROM", "MESSAGE"),
                    MessageEventSTR,
                    messageConverter
            ), Arguments.of(
                    new MessageDTO.Command("MESSAGE"),
                    MessageCommandSTR,
                    messageConverter
            ), Arguments.of(
                    new MessageDTO.Success(),
                    MessageSuccessSTR,
                    messageConverter
            ), Arguments.of(
                    new MessageDTO.Error("XYI"),
                    MessageErrorSTR,
                    messageConverter
            ),

            Arguments.of(
                    new LogoutDTO.Event("USER_NAME"),
                    LogoutEventSTR,
                    logoutConverter
            ), Arguments.of(
                    new LogoutDTO.Command(),
                    LogoutCommandSTR,
                    logoutConverter
            ), Arguments.of(
                    new LogoutDTO.Success(),
                    LogoutSuccessSTR,
                    logoutConverter
            ), Arguments.of(
                    new LogoutDTO.Error("XYI"),
                    LogoutErrorSTR,
                    logoutConverter
            ),

            Arguments.of(
                    new LoginDTO.Event("USER_NAME"),
                    LoginEventSTR,
                    loginConverter
            ), Arguments.of(
                    new LoginDTO.Command("USER_NAME", "PASSWORD"),
                    LoginCommandSTR,
                    loginConverter
            ), Arguments.of(
                    new LoginDTO.Success(),
                    LoginSuccessSTR,
                    loginConverter
            ), Arguments.of(
                    new LoginDTO.Error("XYI"),
                    LoginErrorSTR,
                    loginConverter
            ),

            Arguments.of(
                    new ListDTO.Command(),
                    ListCommandSTR,
                    listConverter
            ), Arguments.of(
                    new ListDTO.Success(List.of(new DataDTO.User("USER_1"), new DataDTO.User("USER_2"))),
                    ListSuccessSTR,
                    listConverter
            ), Arguments.of(
                    new ListDTO.Error("XYI"),
                    ListErrorSTR,
                    listConverter
            )
    );
  }
}
