package com.mans.ecommerce.b2c.controller.advicer;

import java.util.LinkedHashMap;
import java.util.Map;

import com.mans.ecommerce.b2c.domain.exception.LoginException;
import com.mans.ecommerce.b2c.domain.exception.UserAlreadyExistException;
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

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<Object> handleConflictException(Exception ex, WebRequest request)
    {
        return getResponseMessage(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<Object> handleUnauthorized(Exception ex, WebRequest request)
    {
        return getResponseMessage(HttpStatus.UNAUTHORIZED, ex.getMessage());
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
