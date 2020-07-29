package com.mans.ecommerce.b2c.controller.advicer;

import java.util.LinkedHashMap;
import java.util.Map;

import com.mans.ecommerce.b2c.domain.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler
{

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAdvisor.class);

    @ExceptionHandler({ UserAlreadyExistException.class, ConflictException.class })
    public ResponseEntity<Object> handleConflictException(Exception ex, WebRequest request)
    {
        String message = ex.getMessage();
        return getResponseMessage(HttpStatus.CONFLICT, message);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<Object> handleUnauthorized(Exception ex, WebRequest request)
    {
        return getResponseMessage(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler({ ResourceNotFoundException.class })
    public ResponseEntity<Object> handleBadRequest(Exception ex, WebRequest request)
    {
        return getResponseMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler({ SystemConstraintViolation.class, Exception.class })
    public ResponseEntity<Object> handleSystemConstraint(Exception ex, WebRequest request)
    {
        //TODO Send Email
        return getResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request)
    {
        Map<String, Object> body = new LinkedHashMap<>();

        ex.getBindingResult()
          .getFieldErrors()
          .forEach(fieldError -> {
              String fieldName = fieldError.getField();
              String errorMessage = fieldError.getDefaultMessage();
              body.put(fieldName, errorMessage);
          });

        return new ResponseEntity<>(body, status);
    }

    private ResponseEntity<Object> getResponseMessage(HttpStatus status, String message)
    {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}
