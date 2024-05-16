package dto.exceptions;

public class UnsupportedDTOType extends RuntimeException {
  public UnsupportedDTOType(String message) {
    super(message);
  }

  public UnsupportedDTOType(String message, Throwable cause) {
    super(message, cause);
  }

  public UnsupportedDTOType(Throwable cause) {
    super(cause);
  }
}

