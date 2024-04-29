package javachat.client.model.DTO;

import javachat.client.model.DTO.commands.*;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

public class CommandsTest {
  static String testListMarshallingSTR = "<command name=\"list\"/>";
  static String testListUnmarshallingSUCCSTR =
          "<success><users><user><name>USER_1</name></user><user><name>USER_2</name></user></users></success>";
  static String testListUnmarshallingERRSTR = "<error><message>ABxyiAVxyi</message></error>";

  static String testLoginMarshallingSTR = "<command name=\"login\"><name>USER_NAME</name><password>PASSWORD</password></command>";
  static String testLoginUnmarshallingSUCCSTR = "<success></success>";
  static String testLoginUnmarshallingERRSTR = "<error><message>ABxyiAVxyi</message></error>";

  static String testLogoutMarshallingSTR = "<command name=\"logout\"/>";
  static String testLogoutUnmarshallingSUCCSTR = "<success></success>";
  static String testLogoutUnmarshallingERRSTR = "<error><message>ABxyiAVxyi</message></error>";

  static String testMessageMarshallingSTR = "<command name=\"message\"><message>MESSAGE</message></command>";
  static String testMessageUnmarshallingSUCCSTR = "<success></success>";
  static String testMessageUnmarshallingERRSTR = "<error><message>ABxyiAVxyi</message></error>";

  @Test
  public void testListSection() throws JAXBException {
    JAXBContext commandContext = JAXBContext.newInstance(ListSection.Command.class);
    Marshaller comandUmarshaller = commandContext.createMarshaller();
    Unmarshaller comandUnmarshaller = commandContext.createUnmarshaller();

    comandUmarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
    ListSection.Command command = new ListSection.Command();
    StringWriter writer = new StringWriter();
    comandUmarshaller.marshal(command, writer);
    String xmlString = writer.toString();
    Assert.assertEquals(xmlString, testListMarshallingSTR);

    var ex1 = (ListSection.Command) comandUnmarshaller.unmarshal(new StringReader(xmlString));
    Assert.assertEquals(ex1, command);

    JAXBContext responseContext = JAXBContext.newInstance(ListSection.Response.class);
    Unmarshaller responseUnmarshaller = responseContext.createUnmarshaller();
    ListSection.SuccessResponse unmarshaledSuc = (ListSection.SuccessResponse) responseUnmarshaller.unmarshal(new StringReader(testListUnmarshallingSUCCSTR));
    ListSection.ErrorResponse unmarshaledErr = (ListSection.ErrorResponse) responseUnmarshaller.unmarshal(new StringReader(testListUnmarshallingERRSTR));
    var constrErr = new ListSection.ErrorResponse("ABxyiAVxyi");
    Assert.assertEquals(constrErr, unmarshaledErr);
    Assert.assertEquals(constrErr.getStatus(), COMMAND_SECTION.RESPONSE_STATUS.ERROR);

    var constrSuc = new ListSection.SuccessResponse(List.of(new RequestDTO.User("USER_1"), new RequestDTO.User("USER_2")));
    Assert.assertEquals(constrSuc, unmarshaledSuc);
    Assert.assertEquals(constrSuc.getStatus(), COMMAND_SECTION.RESPONSE_STATUS.SUCCESS);
  }

  @Test
  public void testLoginSection() throws JAXBException {
    JAXBContext commandContext = JAXBContext.newInstance(LoginSection.Command.class);
    Marshaller comandUmarshaller = commandContext.createMarshaller();
    Unmarshaller comandUnmarshaller = commandContext.createUnmarshaller();

    comandUmarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
    LoginSection.Command command = new LoginSection.Command("USER_NAME", "PASSWORD");
    StringWriter writer = new StringWriter();
    comandUmarshaller.marshal(command, writer);
    String xmlString = writer.toString();
    Assert.assertEquals(xmlString, testLoginMarshallingSTR);
    var ex1 = (LoginSection.Command) comandUnmarshaller.unmarshal(new StringReader(xmlString));
    Assert.assertEquals(ex1, command);

    JAXBContext responseContext = JAXBContext.newInstance(LoginSection.Response.class);
    Unmarshaller responseUnmarshaller = responseContext.createUnmarshaller();
    LoginSection.SuccessResponse unmarshaledSuc = (LoginSection.SuccessResponse) responseUnmarshaller.unmarshal(new StringReader(testLoginUnmarshallingSUCCSTR));
    LoginSection.ErrorResponse unmarshaledErr = (LoginSection.ErrorResponse) responseUnmarshaller.unmarshal(new StringReader(testLoginUnmarshallingERRSTR));
    var constrErr = new LoginSection.ErrorResponse("ABxyiAVxyi");
    Assert.assertEquals(constrErr, unmarshaledErr);
    Assert.assertEquals(constrErr.getStatus(), COMMAND_SECTION.RESPONSE_STATUS.ERROR);

    var constrSuc = new LoginSection.SuccessResponse();
    Assert.assertEquals(constrSuc, unmarshaledSuc);
    Assert.assertEquals(constrSuc.getStatus(), COMMAND_SECTION.RESPONSE_STATUS.SUCCESS);
  }

  @Test
  public void testLogoutSection() throws JAXBException {
    JAXBContext commandContext = JAXBContext.newInstance(LogoutSection.Command.class);
    Marshaller comandUmarshaller = commandContext.createMarshaller();
    Unmarshaller comandUnmarshaller = commandContext.createUnmarshaller();

    comandUmarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
    LogoutSection.Command command = new LogoutSection.Command();
    StringWriter writer = new StringWriter();
    comandUmarshaller.marshal(command, writer);
    String xmlString = writer.toString();
    Assert.assertEquals(xmlString, testLogoutMarshallingSTR);
    var ex1 = (LogoutSection.Command) comandUnmarshaller.unmarshal(new StringReader(xmlString));
    Assert.assertEquals(ex1, command);

    JAXBContext responseContext = JAXBContext.newInstance(LogoutSection.Response.class);
    Unmarshaller responseUnmarshaller = responseContext.createUnmarshaller();
    LogoutSection.SuccessResponse unmarshaledSuc = (LogoutSection.SuccessResponse) responseUnmarshaller.unmarshal(new StringReader(testLogoutUnmarshallingSUCCSTR));
    LogoutSection.ErrorResponse unmarshaledErr = (LogoutSection.ErrorResponse) responseUnmarshaller.unmarshal(new StringReader(testLogoutUnmarshallingERRSTR));
    var constrErr = new LogoutSection.ErrorResponse("ABxyiAVxyi");
    Assert.assertEquals(constrErr, unmarshaledErr);
    Assert.assertEquals(constrErr.getStatus(), COMMAND_SECTION.RESPONSE_STATUS.ERROR);

    var constrSuc = new LogoutSection.SuccessResponse();
    Assert.assertEquals(constrSuc, unmarshaledSuc);
    Assert.assertEquals(constrSuc.getStatus(), COMMAND_SECTION.RESPONSE_STATUS.SUCCESS);
  }

  @Test
  public void testMessageSection() throws JAXBException {
    JAXBContext commandContext = JAXBContext.newInstance(MessageSection.Command.class);
    Marshaller comandUmarshaller = commandContext.createMarshaller();
    Unmarshaller comandUnmarshaller = commandContext.createUnmarshaller();

    comandUmarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
    MessageSection.Command command = new MessageSection.Command("MESSAGE");
    StringWriter writer = new StringWriter();
    comandUmarshaller.marshal(command, writer);
    String xmlString = writer.toString();
    Assert.assertEquals(xmlString, testMessageMarshallingSTR);
    var ex1 = (MessageSection.Command) comandUnmarshaller.unmarshal(new StringReader(xmlString));
    Assert.assertEquals(ex1, command);

    JAXBContext responseContext = JAXBContext.newInstance(MessageSection.Response.class);
    Unmarshaller responseUnmarshaller = responseContext.createUnmarshaller();
    MessageSection.SuccessResponse unmarshaledSuc = (MessageSection.SuccessResponse) responseUnmarshaller.unmarshal(new StringReader(testMessageUnmarshallingSUCCSTR));
    MessageSection.ErrorResponse unmarshaledErr = (MessageSection.ErrorResponse) responseUnmarshaller.unmarshal(new StringReader(testMessageUnmarshallingERRSTR));
    var constrErr = new MessageSection.ErrorResponse("ABxyiAVxyi");
    Assert.assertEquals(constrErr, unmarshaledErr);
    Assert.assertEquals(constrErr.getStatus(), COMMAND_SECTION.RESPONSE_STATUS.ERROR);

    var constrSuc = new MessageSection.SuccessResponse();
    Assert.assertEquals(constrSuc, unmarshaledSuc);
    Assert.assertEquals(constrSuc.getStatus(), COMMAND_SECTION.RESPONSE_STATUS.SUCCESS);
  }
}
