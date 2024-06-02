package dto;

import dto.exceptions.UnableToDeserialize;
import dto.exceptions.UnableToSerialize;
import dto.interfaces.DTOConverter;
import dto.interfaces.DTOConverterManagerInterface;
import dto.interfaces.DTOInterfaces;
import dto.subtypes.file.*;
import dto.subtypes.list.ListCommand;
import dto.subtypes.list.ListDTOConverter;
import dto.subtypes.list.ListError;
import dto.subtypes.list.ListSuccess;
import dto.subtypes.login.*;
import dto.subtypes.logout.*;
import dto.subtypes.message.*;
import dto.subtypes.other.User;
import jakarta.xml.bind.JAXBException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DTOTest {
  static String ListCommandSTR = "<command name=\"list\"/>";
  static String ListSuccessSTR = "<success><users><user><name>USER_1</name></user><user><name>USER_2</name></user></users></success>";
  static String ListSuccessSTR2 = "<success><users><user><name>6</name></user></users></success>";
  static String ListErrorSTR = "<error><message>XYI</message></error>";

  static String LoginEventSTR = "<event name=\"userlogin\"><name>USER NAME</name></event>";
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

  static String FileUploadTryCommandSTR = "<command name=\"upload\"><content>QmFzZTY0LWVuY29kZWQgZmlsZSBjb250ZW50</content><encoding>base64</encoding><mimeType>text/plain</mimeType><name>file name</name></command>";
  static String FileUploadCommandSTR = "<command name=\"upload\"><name>file name</name><mimeType>text/plain</mimeType><encoding>base64</encoding><content>QmFzZTY0LWVuY29kZWQgZmlsZSBjb250ZW50</content></command>";
  static String FileDownloadCommandSTR = "<command name=\"download\"><id>0</id></command>";
  static String FileUploadSuccessSTR = "<success><id>0</id></success>";
  static String FileDownloadSuccessSTR = "<success><id>0</id><name>file name</name><mimeType>text/plain</mimeType><encoding>base64</encoding><content>QmFzZTY0LWVuY29kZWQgZmlsZSBjb250ZW50</content></success>";
  static String FileErrorSTR = "<error><message>REASON</message></error>";
  static String FileEventSTR = "<event name=\"file\"><id>0</id><from>CHAT_NAME_FROM</from><name>file name</name><size>0</size><mimeType>text/plain</mimeType></event>";

  static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  static ListDTOConverter listDTOConverter;
  static MessageDTOConverter messageDTOConverter;
  static LogoutDTOConverter logoutDTOConverter;
  static LoginDTOConverter loginDTOConverter;
  static FileUploadDTOConverter fileUploadConverter;
  static FileDownloadDTOConverter fileDownloadDTOConverter;
  static dto.DTOConverterManager manager;


  @BeforeAll
  public void prepare() throws JAXBException {
    loginDTOConverter = new LoginDTOConverter();
    logoutDTOConverter = new LogoutDTOConverter();
    listDTOConverter = new ListDTOConverter();
    fileUploadConverter = new FileUploadDTOConverter();
    fileDownloadDTOConverter = new FileDownloadDTOConverter();
    messageDTOConverter = new MessageDTOConverter();
    manager = new dto.DTOConverterManager(null);
  }

  @Test
  public void ABXyi() {
  }

  @ParameterizedTest
  @MethodSource("CommandDTOTest")
  public void CommandTest(DTOInterfaces.COMMAND_DTO expectedCommand, String expectedSTR,
                          RequestDTO.DTO_SECTION section, RequestDTO.DTO_TYPE type) throws UnableToSerialize, UnableToDeserialize {
    var converter = manager.getConverterBySection(expectedCommand.getCommandType().geDTOSection());
    var serCommand = converter.serialize(expectedCommand);
    var tree = manager.getXMLTree(serCommand.getBytes());
    var deserComm = converter.deserialize(tree);
    Assert.assertEquals(expectedSTR, serCommand);
    Assert.assertEquals(expectedCommand, deserComm);
    Assert.assertEquals(type, DTOConverterManagerInterface.getDTOType(tree));
  }

  public Stream<Arguments> CommandDTOTest() throws UnableToSerialize, UnableToDeserialize, JAXBException {
    manager = new dto.DTOConverterManager(null);
    return Stream.of(
            Arguments.of(
                    new MessageCommand("MESSAGE"),
                    MessageCommandSTR,
                    RequestDTO.DTO_SECTION.MESSAGE,
                    RequestDTO.DTO_TYPE.COMMAND
            ),

            Arguments.of(
                    new LogoutCommand(),
                    LogoutCommandSTR,
                    RequestDTO.DTO_SECTION.LOGOUT,
                    RequestDTO.DTO_TYPE.COMMAND
            ),

            Arguments.of(
                    new LoginCommand("USER_NAME", "PASSWORD"),
                    LoginCommandSTR,
                    RequestDTO.DTO_SECTION.LOGIN,
                    RequestDTO.DTO_TYPE.COMMAND
            ),

            Arguments.of(
                    new ListCommand(),
                    ListCommandSTR,
                    RequestDTO.DTO_SECTION.LIST,
                    RequestDTO.DTO_TYPE.COMMAND
            ),

            /*Arguments.of(
                    new FileDTO.UploadCommand("file name", "text/plain", "base64", "Base64-encoded file content".getBytes()),
                    FileUploadTryCommandSTR,
                    RequestDTO.DTO_SECTION.FILE,
                    RequestDTO.DTO_TYPE.COMMAND
            ),*/
            Arguments.of(
                    new FileUploadCommand("file name", "text/plain", "base64", "Base64-encoded file content".getBytes()),
                    FileUploadCommandSTR,
                    RequestDTO.DTO_SECTION.FILE,
                    RequestDTO.DTO_TYPE.COMMAND
            ), Arguments.of(
                    new FileDownloadCommand(0L),
                    FileDownloadCommandSTR,
                    RequestDTO.DTO_SECTION.FILE,
                    RequestDTO.DTO_TYPE.COMMAND
            )
    );
  }

  @ParameterizedTest
  @MethodSource("EventsDTOTest")
  public void EventsTest(DTOInterfaces.EVENT_DTO expectedEvent, String expectedSTR,
                         RequestDTO.EVENT_TYPE section,
                         RequestDTO.DTO_TYPE type,
                         DTOConverter converter) throws UnableToSerialize, UnableToDeserialize {
    var serEvent = converter.serialize(expectedEvent);
    var tree = manager.getXMLTree(serEvent.getBytes());
    var deserDTO = converter.deserialize(manager.getXMLTree(serEvent.getBytes()));
    Assert.assertEquals(section, DTOConverterManagerInterface.getDTOEvent(tree));
    Assert.assertEquals(type, DTOConverterManagerInterface.getDTOType(tree));
    Assert.assertEquals(expectedSTR, serEvent);
//    Assert.assertTrue(expectedEvent.equals(deserDTO));
    Assert.assertEquals(expectedEvent, deserDTO);
  }

  public Stream<Arguments> EventsDTOTest() throws UnableToSerialize {
    return Stream.of(
            Arguments.of(
                    new MessageEvent("CHAT_NAME_FROM", "MESSAGE"),
                    MessageEventSTR,
                    RequestDTO.EVENT_TYPE.MESSAGE,
                    RequestDTO.DTO_TYPE.EVENT,
                    messageDTOConverter
            ), Arguments.of(
                    new LogoutEvent("USER_NAME"),
                    LogoutEventSTR,
                    RequestDTO.EVENT_TYPE.USERLOGOUT,
                    RequestDTO.DTO_TYPE.EVENT,
                    logoutDTOConverter
            ), Arguments.of(
                    new LoginEvent("USER NAME"),
                    LoginEventSTR,
                    RequestDTO.EVENT_TYPE.USERLOGIN,
                    RequestDTO.DTO_TYPE.EVENT,
                    loginDTOConverter
            ), Arguments.of(
                    new FileEvent(0L, "CHAT_NAME_FROM", "file name", 0, "text/plain"),
                    FileEventSTR,
                    RequestDTO.EVENT_TYPE.FILE,
                    RequestDTO.DTO_TYPE.EVENT,
                    fileUploadConverter
            )
    );
  }

  @ParameterizedTest
  @MethodSource("ResponseDTOTest")
  public void ResponseTest(DTOInterfaces.RESPONSE_DTO expectedResponse,
                           String expectedStr, DTOConverter converter) throws IOException {
    var serEvent = converter.serialize(expectedResponse);
    var serResp = converter.deserialize(manager.getXMLTree(serEvent));
    Assert.assertEquals(expectedStr, serEvent);
    Assert.assertEquals(expectedResponse, serResp);
  }

  public Stream<Arguments> ResponseDTOTest() throws JAXBException {
    manager = new dto.DTOConverterManager(null);
    return Stream.of(
            Arguments.of(
                    new MessageSuccess(),
                    MessageSuccessSTR, messageDTOConverter
            ), Arguments.of(
                    new MessageError("XYI"),
                    MessageErrorSTR, messageDTOConverter
            ),

            Arguments.of(
                    new LogoutSuccess(),
                    LogoutSuccessSTR, logoutDTOConverter
            ), Arguments.of(
                    new LogoutError("XYI"),
                    LogoutErrorSTR, logoutDTOConverter
            ),

            Arguments.of(
                    new LoginSuccess(),
                    LoginSuccessSTR, loginDTOConverter
            ), Arguments.of(
                    new LoginError("XYI"),
                    LoginErrorSTR, loginDTOConverter
            ),

            Arguments.of(
                    new ListSuccess(List.of(new User("USER_1"), new User("USER_2"))),
                    ListSuccessSTR, listDTOConverter
            ), Arguments.of(
                    new ListSuccess(List.of(new User("6"))),
                    ListSuccessSTR2, listDTOConverter
            ), Arguments.of(
                    new ListError("XYI"),
                    ListErrorSTR, listDTOConverter
            ),
/*
            Arguments.of(
                    new RequestDTO.BaseErrorResponse(RequestDTO.DTO_SECTION.BASE, "XYI"),
                    LoginErrorSTR,
                    manager
            ), Arguments.of(
                    new RequestDTO.BaseSuccessResponse(RequestDTO.DTO_SECTION.BASE),
                    LoginSuccessSTR,
                    manager
            ),*/

            Arguments.of(
                    new FileUploadSuccess(0L),
                    FileUploadSuccessSTR, fileUploadConverter
            ), Arguments.of(
                    new FileDownloadSuccess(0L, "file name", "text/plain", "base64", "Base64-encoded file content".getBytes()),
                    FileDownloadSuccessSTR, fileDownloadDTOConverter
            ), Arguments.of(
                    new FileError("REASON"),
                    FileErrorSTR, fileUploadConverter
            )
    );
  }
}
