package by.egorivanov.cloudstorage.advice;

import by.egorivanov.cloudstorage.dto.response.ErrorMessageResponse;
import by.egorivanov.cloudstorage.exception.MoveResourceException;
import by.egorivanov.cloudstorage.exception.RenameResourceException;
import by.egorivanov.cloudstorage.exception.SearchException;
import by.egorivanov.cloudstorage.exception.StorageException;
import by.egorivanov.cloudstorage.exception.UploadException;
import by.egorivanov.cloudstorage.exception.UserAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({MoveResourceException.class, RenameResourceException.class, SearchException.class})
    public ResponseEntity<ErrorMessageResponse> handleBadRequest(RuntimeException ex) {
        log.error("Resource operation error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(UploadException.class)
    public ResponseEntity<ErrorMessageResponse> handleUploadException(UploadException ex) {
        log.error("Upload error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorMessageResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        log.warn("User already exists: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorMessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorMessageResponse> handleStorageException(StorageException ex) {
        log.error("Storage error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessageResponse("Internal server error"));
    }
}
