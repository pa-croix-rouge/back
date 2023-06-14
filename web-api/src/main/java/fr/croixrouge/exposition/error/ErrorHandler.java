package fr.croixrouge.exposition.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public abstract class ErrorHandler {
    private final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler(value = {NoSuchElementException.class})
    public ResponseEntity<?> manageSimpleServiceException(final NoSuchElementException simpleServiceException) {
        simpleServiceException.printStackTrace();
        logger.info("NoSuchElementException: " + simpleServiceException.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ex.printStackTrace();
        if (ex instanceof NullPointerException) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
