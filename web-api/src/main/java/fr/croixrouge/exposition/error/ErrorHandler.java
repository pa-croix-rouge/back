package fr.croixrouge.exposition.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

// @ControllerAdvice
public abstract class ErrorHandler {

    @ExceptionHandler(value = {NoSuchElementException.class})
    public ResponseEntity<?> manageSimpleServiceException(final NoSuchElementException simpleServiceException) {
        return ResponseEntity.notFound().build();
    }
}
