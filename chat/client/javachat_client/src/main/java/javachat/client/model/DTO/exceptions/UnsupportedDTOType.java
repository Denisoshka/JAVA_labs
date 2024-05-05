package javachat.client.model.DTO.exceptions;

public class UnsupportedDTOType extends RuntimeException {
  public UnsupportedDTOType(String message) {
    super("Unsupported DTO type" + message);
  }
}
