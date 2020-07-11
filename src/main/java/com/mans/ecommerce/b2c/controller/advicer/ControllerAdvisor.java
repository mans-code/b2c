package com.mans.ecommerce.b2c.controller.advicer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public ResponseEntity<Object> handle(Exception ex, WebRequest request)
    {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request)
    {

        Map<String, Object> body = new LinkedHashMap<>();

        List<String> errors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(x -> x.getDefaultMessage())
                                .collect(Collectors.toList());

        return new ResponseEntity<>(errors, status);
    }

}
