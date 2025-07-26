package by.egorivanov.cloudstorage.exception;

public class RenameResourceException extends RuntimeException {
  public RenameResourceException(String message, Exception e) {
    super(message);
  }
}
