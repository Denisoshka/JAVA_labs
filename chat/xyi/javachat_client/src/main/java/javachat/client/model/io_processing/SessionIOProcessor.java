package javachat.client.model.io_processing;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class SessionIOProcessor implements IOProcessor {
  private final ReentrantLock readLock = new ReentrantLock();
  private final ReentrantLock writeLock = new ReentrantLock();
  private final DataInputStream inputStream;
  private final DataOutputStream outputStream;

  public SessionIOProcessor(DataInputStream inputStream, DataOutputStream outputStream) {
    this.inputStream = inputStream;
    this.outputStream = outputStream;
  }

  @Override
  public byte[] receiveMessage() throws IOException {
    int len = inputStream.readInt();
    return inputStream.readNBytes(len);
  }

  @Override
  public void sendMessage(String xml) throws IOException {
    int len = xml.getBytes().length;
    outputStream.writeInt(len);
    outputStream.write(xml.getBytes(), 0, len);
  }

  public ReentrantLock getReadLock() {
    return readLock;
  }

  public ReentrantLock getWriteLock() {
    return writeLock;
  }
}
