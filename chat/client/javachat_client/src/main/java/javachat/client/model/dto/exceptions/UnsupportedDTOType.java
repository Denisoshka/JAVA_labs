package javachat.client.model.dto.exceptions;

public class UnsupportedDTOType extends RuntimeException {
  public UnsupportedDTOType(String message) {
    super("Unsupported DTO type" + message);
  }
}
