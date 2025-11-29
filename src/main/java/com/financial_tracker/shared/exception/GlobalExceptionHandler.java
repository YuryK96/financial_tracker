package com.financial_tracker.shared.exception;

import com.financial_tracker.shared.dto.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        log.error("Handle Exception: ", e);

        ErrorResponse error = new ErrorResponse(
                "Internal server error",
                new String[]{e.getMessage()},
                LocalDateTime.now()

        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);

    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {

        log.error("Handle handleEntityNotFoundException: ", e);

        ErrorResponse error = new ErrorResponse(
                "Not Found",
                new String[]{e.getMessage()},
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(exception = {
            IllegalStateException.class,
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ErrorResponse> handleBardRequest(Exception e) {
        log.error("Handle handleBardRequest: ", e);

        String[] messages;

        if (e instanceof MethodArgumentNotValidException) {

            List<ObjectError> errors = ((MethodArgumentNotValidException) e).getAllErrors();

            messages = errors.stream()
                    .map(error -> {

                        if (error instanceof FieldError fieldError) {
                            return fieldError.getField() + ": " + fieldError.getDefaultMessage();
                        } else {
                            return error.getDefaultMessage();
                        }

                    })
                    .toArray(String[]::new);


        } else {
            messages = new String[]{e.getMessage()};
        }

        ErrorResponse error = new ErrorResponse(
                "Illegal arguments",
                messages,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }


}
