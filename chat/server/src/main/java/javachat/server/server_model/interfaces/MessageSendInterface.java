package javachat.server.server_model.interfaces;

import java.io.DataOutputStream;
import java.io.IOException;

public interface MessageSendInterface {
  default void receiveMessage(String message, DataOutputStream outStream) throws IOException {
    int msgLen = message.getBytes().length;
    outStream.write(msgLen);
    outStream.write(message.getBytes(), 0, msgLen);
    outStream.flush();
  }
}

