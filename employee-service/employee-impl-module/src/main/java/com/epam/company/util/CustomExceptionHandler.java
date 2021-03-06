package com.epam.company.util;

import com.epam.company.exception.NoSuchElementInDBException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.validation.ValidationException;
import java.net.ConnectException;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NoSuchElementInDBException.class)
    protected ResponseEntity<ErrorInfo> handleNoSuchElementInDBException(NoSuchElementInDBException ex) {
        return new ResponseEntity<>(new ErrorInfo(ex.getLocalizedMessage())
                , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<ErrorInfo> handleValidationException(ValidationException ex) {
        return new ResponseEntity<>(new ErrorInfo(ex.getLocalizedMessage())
                , HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ConnectException.class)
    protected ResponseEntity<ErrorInfo> handleConnectException(ConnectException ex) {
        return new ResponseEntity<>(new ErrorInfo(ex.getLocalizedMessage())
                , HttpStatus.BAD_GATEWAY);
    }

    private class ErrorInfo {
        String description;

        public ErrorInfo() {
        }

        public ErrorInfo(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
