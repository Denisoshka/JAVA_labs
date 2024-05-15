package io_processing;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.locks.ReentrantLock;

public class IOProcessor implements AbstractIOProcessor, AutoCloseable {
  private final ReentrantLock readLock = new ReentrantLock();
  private final ReentrantLock writeLock = new ReentrantLock();
  private final DataInputStream inputStream;
  private final DataOutputStream outputStream;
  private final Socket socket;

  /**
   * create IOProcessor with specified {@code socket} and {@code readTimeout}
   */
  public IOProcessor(Socket socket, int readTimeout) throws IOException {
    this.inputStream = new DataInputStream(socket.getInputStream());
    this.outputStream = new DataOutputStream(socket.getOutputStream());
    this.socket = socket;
    if (readTimeout > 0) {
      socket.setSoTimeout(readTimeout);
    }
  }

  @Override
  public byte[] receiveMessage() throws IOException {
    synchronized (inputStream) {
      try {
        int len = inputStream.readInt();
        return inputStream.readNBytes(len);
      } catch (EOFException _) {
        return null;
      }
    }
  }

  @Override
  public void sendMessage(byte[] message) throws IOException {
    synchronized (outputStream) {
      int len = message.length;
      outputStream.writeInt(len);
      outputStream.write(message, 0, len);
    }
  }

  @Override
  public boolean isClosed() {
    return socket.isClosed();
  }

  @Override
  public void setSoTimeout(int timeout) throws SocketException {
    socket.setSoTimeout(timeout);
  }


  @Override
  public void close() throws IOException {

    IOException startEx = null;
    try {
      socket.close();
    } catch (IOException e) {
      startEx = e;
    } finally {
      try {
        inputStream.close();
      } catch (IOException exI) {
        if (startEx != null) startEx.addSuppressed(exI);
        else startEx = exI;
      }
      try {
        outputStream.close();
      } catch (IOException exO) {
        if (startEx != null) startEx.addSuppressed(exO);
        else startEx = exO;
      }
      if (startEx != null) throw startEx;
    }
  }
}
