package by.egorivanov.cloudstorage.exception;

public class MoveResourceException extends RuntimeException {
    public MoveResourceException(String message, Exception e) {
        super(message);
    }
}
