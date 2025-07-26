package by.egorivanov.cloudstorage.exception;

public class UploadException extends RuntimeException {
    public UploadException(String message, Exception e) {
        super(message);
    }

    public UploadException(String message) {
        super(message);
    }
}
