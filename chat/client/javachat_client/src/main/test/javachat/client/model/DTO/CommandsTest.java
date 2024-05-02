package javachat.client.model.DTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import javachat.client.model.DTO.commands.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

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

  static PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
          .allowIfSubType("javachat.client.model.DTO.commands")
          .build();
  static XmlMapper xmlMapper = new XmlMapper();

  @BeforeAll
  static void initMapper() {
    xmlMapper.activateDefaultTyping(ptv);
  }

  @Test
  public void testListSection() throws JsonProcessingException {
    ListSection.Command command = new ListSection.Command();
    String xmlString = xmlMapper.writeValueAsString(command);
    Assert.assertEquals(xmlString, testListMarshallingSTR);
    var ex1 = (ListSection.Command) xmlMapper.readValue(xmlString, CommandSection.Command.class);
    Assert.assertEquals(ex1, command);

    ListSection.ErrorResponse unmarshaledErr = (ListSection.ErrorResponse) xmlMapper.readValue(testListUnmarshallingERRSTR, ListSection.Response.class);
    var constrErr = new ListSection.ErrorResponse("ABxyiAVxyi");
    Assert.assertEquals(constrErr, unmarshaledErr);
    Assert.assertEquals(constrErr.getStatus(), CommandSection.RESPONSE_STATUS.ERROR);

    ListSection.SuccessResponse unmarshaledSuc = (ListSection.SuccessResponse) xmlMapper.readValue(testListUnmarshallingSUCCSTR, ListSection.Response.class);
    var constrSuc = new ListSection.SuccessResponse(List.of(new RequestDTO.User("USER_1"), new RequestDTO.User("USER_2")));
    Assert.assertEquals(constrSuc, unmarshaledSuc);
    Assert.assertEquals(constrSuc.getStatus(), CommandSection.RESPONSE_STATUS.SUCCESS);
  }

  @Test
  public void testLoginSection() throws JsonProcessingException {
    LoginSection.Command command = new LoginSection.Command("USER_NAME", "PASSWORD");
    String xmlString = xmlMapper.writeValueAsString(command);
    Assert.assertEquals(xmlString, testLoginMarshallingSTR);
    var ex1 = (LoginSection.Command) xmlMapper.readValue(xmlString, CommandSection.Command.class);
    Assert.assertEquals(ex1, command);

    LoginSection.SuccessResponse unmarshaledSuc = (LoginSection.SuccessResponse) xmlMapper.readValue(testLoginUnmarshallingSUCCSTR, LoginSection.Response.class);
    LoginSection.ErrorResponse unmarshaledErr = (LoginSection.ErrorResponse) xmlMapper.readValue(testLoginUnmarshallingERRSTR, LoginSection.Response.class);
    var constrErr = new LoginSection.ErrorResponse("ABxyiAVxyi");
    Assert.assertEquals(constrErr, unmarshaledErr);
    Assert.assertEquals(constrErr.getStatus(), CommandSection.RESPONSE_STATUS.ERROR);

    var constrSuc = new LoginSection.SuccessResponse();
    Assert.assertEquals(constrSuc, unmarshaledSuc);
    Assert.assertEquals(constrSuc.getStatus(), CommandSection.RESPONSE_STATUS.SUCCESS);
  }

  @Test
  public void testLogoutSection() throws JsonProcessingException {
    LogoutSection.Command command = new LogoutSection.Command();
    String xmlString = xmlMapper.writeValueAsString(command);
    Assert.assertEquals(xmlString, testLogoutMarshallingSTR);
    var ex1 = xmlMapper.readValue(xmlString, CommandSection.Command.class);
    Assert.assertEquals(ex1, command);

    LogoutSection.ErrorResponse unmarshaledErr = (LogoutSection.ErrorResponse) xmlMapper.readValue(testLogoutUnmarshallingERRSTR, LogoutSection.Response.class);
    var constrErr = new LogoutSection.ErrorResponse("ABxyiAVxyi");
    Assert.assertEquals(constrErr, unmarshaledErr);
    Assert.assertEquals(constrErr.getStatus(), CommandSection.RESPONSE_STATUS.ERROR);

    LogoutSection.SuccessResponse unmarshaledSuc = (LogoutSection.SuccessResponse) xmlMapper.readValue(testLogoutUnmarshallingSUCCSTR, LogoutSection.Response.class);
    var constrSuc = new LogoutSection.SuccessResponse();
    Assert.assertEquals(constrSuc, unmarshaledSuc);
    Assert.assertEquals(constrSuc.getStatus(), CommandSection.RESPONSE_STATUS.SUCCESS);
  }

  @Test
  public void testMessageSection() throws JsonProcessingException {
    MessageSection.Command command = new MessageSection.Command("MESSAGE");
    String xmlString = xmlMapper.writeValueAsString(command);
    Assert.assertEquals(xmlString, testMessageMarshallingSTR);
    var ex1 = (MessageSection.Command) xmlMapper.readValue(xmlString, CommandSection.Command.class);
    Assert.assertEquals(ex1, command);

    MessageSection.ErrorResponse unmarshaledErr = (MessageSection.ErrorResponse) xmlMapper.readValue(testMessageUnmarshallingERRSTR, MessageSection.Response.class);
    var constrErr = new MessageSection.ErrorResponse("ABxyiAVxyi");
    Assert.assertEquals(constrErr, unmarshaledErr);
    Assert.assertEquals(constrErr.getStatus(), CommandSection.RESPONSE_STATUS.ERROR);

    MessageSection.SuccessResponse unmarshaledSuc = (MessageSection.SuccessResponse) xmlMapper.readValue(testMessageUnmarshallingSUCCSTR, MessageSection.Response.class);
    var constrSuc = new MessageSection.SuccessResponse();
    Assert.assertEquals(constrSuc, unmarshaledSuc);
    Assert.assertEquals(constrSuc.getStatus(), CommandSection.RESPONSE_STATUS.SUCCESS);
  }
}
