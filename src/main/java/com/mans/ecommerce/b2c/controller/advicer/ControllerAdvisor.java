package com.mans.ecommerce.b2c.controller.advicer;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mans.ecommerce.b2c.domain.exception.*;
import com.mans.ecommerce.b2c.utill.Emailing;
import com.stripe.exception.StripeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    private Emailing emailing;

    private ObjectMapper mapper;

    private String to;

    ControllerAdvisor(Emailing emailing, ObjectMapper mapper, @Value("${app.crash.email}") String to)
    {
        this.emailing = emailing;
        this.to = to;
    }

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

    @ExceptionHandler({ PaymentFailedException.class, StripeException.class })
    public ResponseEntity<Object> handlePaymentError(Exception ex, WebRequest request)
    {

        return getResponseMessage(HttpStatus.FAILED_DEPENDENCY, ex.getMessage());
    }

    @ExceptionHandler({ SystemConstraintViolation.class, Exception.class })
    public ResponseEntity<Object> handleSystemConstraint(Exception ex, WebRequest request)
    {
        sendEmail(ex);
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

    private void sendEmail(Exception ex)
    {
        String subject = "APP Crash!!!!!!!!!!!!";
        String body_template = "Time=%s\n"
                                       + "exception=%s";
        String exception = getJson(ex);
        String time = LocalDate.now().toString();
        String body = String.format(body_template, time, exception);
        emailing.sendEmail(to, subject, body);
    }

    private String getJson(Exception ex)
    {
        try
        {
            return mapper.writeValueAsString(ex);
        }
        catch (JsonProcessingException e)
        {
            return ex.getMessage();
        }
    }
}
