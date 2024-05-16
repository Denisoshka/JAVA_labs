package dto;

import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
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
  static String ListSuccessSTR2 = "<success><users><user><name>6</name></user></users></success>";
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
  static ListDTO.ListDTOConverter listDTOConverter;
  static MessageDTO.MessageDTOConverter messageDTOConverter;
  static LogoutDTO.LogoutDTOConverter logoutDTOConverter;
  static LoginDTO.LoginDTOConverter loginDTOConverter;
  static DTOConverterManager manager;

  @Before
  public void prepare() throws ParserConfigurationException, JAXBException {
    listDTOConverter = new ListDTO.ListDTOConverter();
    loginDTOConverter = new LoginDTO.LoginDTOConverter();
    logoutDTOConverter = new LogoutDTO.LogoutDTOConverter();
    messageDTOConverter = new MessageDTO.MessageDTOConverter();
  }

  @Test
  public void ABXyi() {
  }

  @ParameterizedTest
  @MethodSource("CommandDTOTest")
  public void SectionTest(String eventto, RequestDTO.DTO_SECTION section, RequestDTO.DTO_TYPE type) throws UnableToSerialize, UnableToDeserialize {
    var tree = manager.getXMLTree(eventto.getBytes());
    Assert.assertEquals(section, manager.getDTOSection(tree));
    Assert.assertEquals(type, manager.getDTOType(tree));
  }

  public static Stream<Arguments> CommandDTOTest() throws UnableToSerialize, UnableToDeserialize {
    manager = new DTOConverterManager(null);
//    new LoginDTO.Command("xyi", "xyi")
    return Stream.of(
            Arguments.of(
                    manager.serialize(new MessageDTO.Command("MESSAGE")),
                    RequestDTO.DTO_SECTION.MESSAGE,
                    RequestDTO.DTO_TYPE.COMMAND
            ),

            Arguments.of(
                    manager.serialize(new LogoutDTO.Command()),
                    RequestDTO.DTO_SECTION.LOGOUT,
                    RequestDTO.DTO_TYPE.COMMAND
            ),

            Arguments.of(
                    manager.serialize(new LoginDTO.Command("USER_NAME", "PASSWORD")),
                    RequestDTO.DTO_SECTION.LOGIN,
                    RequestDTO.DTO_TYPE.COMMAND
            ),

            Arguments.of(
                    manager.serialize(new ListDTO.Command()),
                    RequestDTO.DTO_SECTION.LIST,
                    RequestDTO.DTO_TYPE.COMMAND
            )
    );
  }

  @ParameterizedTest
  @MethodSource("EventsDTOTest")
  public void EventsTest(String eventto, RequestDTO.BaseEvent.EVENT_TYPE section, RequestDTO.DTO_TYPE type) throws UnableToSerialize, UnableToDeserialize {
    var tree = manager.getXMLTree(eventto.getBytes());
    Assert.assertEquals(section, manager.getDTOEvent(tree));
    Assert.assertEquals(type, manager.getDTOType(tree));
  }

  public static Stream<Arguments> EventsDTOTest() throws UnableToSerialize, UnableToDeserialize {
    manager = new DTOConverterManager(null);
//    new LoginDTO.Command("xyi", "xyi")
    return Stream.of(Arguments.of(
                    manager.serialize(new MessageDTO.Event("CHAT_NAME_FROM", "MESSAGE")),
                    RequestDTO.BaseEvent.EVENT_TYPE.MESSAGE,
                    RequestDTO.DTO_TYPE.EVENT
            ),
            Arguments.of(
                    manager.serialize(new LogoutDTO.Event("USER_NAME")),
                    RequestDTO.BaseEvent.EVENT_TYPE.USERLOGOUT,
                    RequestDTO.DTO_TYPE.EVENT
            ),
            Arguments.of(
                    manager.serialize(new LoginDTO.Event("USER_NAME")),
                    RequestDTO.BaseEvent.EVENT_TYPE.USERLOGIN,
                    RequestDTO.DTO_TYPE.EVENT
            ));
  }

  @ParameterizedTest
  @MethodSource("ArgsConvertingDTOTest")
  public void ConvertingDTOTest(RequestDTO eventto, String eventXml, DTOConverterManager manager) throws JAXBException, IOException, SAXException, ParserConfigurationException {
    DocumentBuilder builder = Objects.requireNonNull(factory.newDocumentBuilder());
    RequestDTO.BaseDTOConverter converter = manager.getConverter(eventto.geDTOSection());
    String serializableXML = manager.serialize(eventto);
    Assert.assertEquals(eventXml, serializableXML);

    RequestDTO eventfrom = converter.deserialize(
            converter.getXMLTree(builder, eventXml.getBytes())
    );
    Assert.assertEquals(eventto, eventfrom);
  }

  public static Stream<Arguments> ArgsConvertingDTOTest() throws JAXBException {
    manager = new DTOConverterManager(null);
    return Stream.of(
            Arguments.of(
                    new MessageDTO.Event("CHAT_NAME_FROM", "MESSAGE"),
                    MessageEventSTR,
                    manager
            ), Arguments.of(
                    new MessageDTO.Command("MESSAGE"),
                    MessageCommandSTR,
                    manager
            ), Arguments.of(
                    new MessageDTO.Success(),
                    MessageSuccessSTR,
                    manager
            ), Arguments.of(
                    new MessageDTO.Error("XYI"),
                    MessageErrorSTR,
                    manager
            ),

            Arguments.of(
                    new LogoutDTO.Event("USER_NAME"),
                    LogoutEventSTR,
                    manager
            ), Arguments.of(
                    new LogoutDTO.Command(),
                    LogoutCommandSTR,
                    manager
            ), Arguments.of(
                    new LogoutDTO.Success(),
                    LogoutSuccessSTR,
                    manager
            ), Arguments.of(
                    new LogoutDTO.Error("XYI"),
                    LogoutErrorSTR,
                    manager
            ),

            Arguments.of(
                    new LoginDTO.Event("USER_NAME"),
                    LoginEventSTR,
                    manager
            ), Arguments.of(
                    new LoginDTO.Command("USER_NAME", "PASSWORD"),
                    LoginCommandSTR,
                    manager
            ), Arguments.of(
                    new LoginDTO.Success(),
                    LoginSuccessSTR,
                    manager
            ), Arguments.of(
                    new LoginDTO.Error("XYI"),
                    LoginErrorSTR,
                    manager
            ),

            Arguments.of(
                    new ListDTO.Command(),
                    ListCommandSTR,
                    manager
            ), Arguments.of(
                    new ListDTO.Success(List.of(new DataDTO.User("USER_1"), new DataDTO.User("USER_2"))),
                    ListSuccessSTR,
                    manager
            ), Arguments.of(
                    new ListDTO.Success(List.of(new DataDTO.User("6"))),
                    ListSuccessSTR2,
                    manager
            ), Arguments.of(
                    new ListDTO.Error("XYI"),
                    ListErrorSTR,
                    manager
            ),

            Arguments.of(
                    new RequestDTO.BaseErrorResponse(RequestDTO.DTO_SECTION.BASE, "XYI"),
                    LoginErrorSTR,
                    manager
            ), Arguments.of(
                    new RequestDTO.BaseSuccessResponse(RequestDTO.DTO_SECTION.BASE),
                    LoginSuccessSTR,
                    manager
            )
    );
  }
}
