package com.mans.ecommerce.b2c.controller.advicer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mans.ecommerce.b2c.domain.exception.*;
import com.mans.ecommerce.b2c.server.eventListener.entity.ServerErrorEvent;
import com.stripe.exception.StripeException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

@RestControllerAdvice
public class ControllerAdviser
{
    private ApplicationEventPublisher publisher;

    ControllerAdviser(ApplicationEventPublisher publisher)
    {
        this.publisher = publisher;
    }

    @ExceptionHandler({ UserAlreadyExistException.class, ConflictException.class })
    @ResponseBody
    public ResponseEntity<Object> handleConflictException(Exception ex)
    {
        return getResponseMessage(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler({ UnauthorizedException.class, BadCredentialsException.class })
    @ResponseBody
    public ResponseEntity<Object> handleUnauthorized(Exception ex)
    {
        return getResponseMessage(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler({ ResourceNotFoundException.class })
    @ResponseBody
    public ResponseEntity<Object> handleBadRequest(Exception ex)
    {
        return getResponseMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseBody
    public ResponseEntity<Object> handleBadRequest(WebExchangeBindException ex)
    {
        Map<String, String> body = new HashMap<>();

        ex.getFieldErrors()
          .forEach(fieldError ->
                   {
                       String fieldName = fieldError.getField();
                       String errorMessage = fieldError.getDefaultMessage();
                       body.put(fieldName, errorMessage);
                   });

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ PaymentFailedException.class, StripeException.class })
    @ResponseBody
    public ResponseEntity<Object> handlePaymentError(Exception ex)
    {
        return getResponseMessage(HttpStatus.FAILED_DEPENDENCY, ex.getMessage());
    }

    @ExceptionHandler({ OutOfStockException.class })
    @ResponseBody
    public ResponseEntity<Object> outOfStockException(OutOfStockException ex)
    {
        return getResponseMessage(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({ PartialOutOfStockException.class })
    @ResponseBody
    public ResponseEntity<Object> partialStockException(PartialOutOfStockException ex)
    {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", ex.getMessage());
        body.put("cart", ex.getCart());
        return new ResponseEntity<>(body, HttpStatus.PARTIAL_CONTENT);
    }

    @ExceptionHandler({ UncompletedCheckoutException.class })
    @ResponseBody
    public ResponseEntity<Object> uncompletedCheckoutException(UncompletedCheckoutException ex)
    {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", ex.getMessage());
        body.put("uncompleted", ex.getUncompleted());
        body.put("checkoutResponse", ex.getCheckoutResponse());
        return new ResponseEntity<>(body, HttpStatus.PARTIAL_CONTENT);
    }

    @ExceptionHandler({ SystemConstraintViolation.class, Exception.class, RuntimeException.class })
    @ResponseBody
    public ResponseEntity<Object> handleSystemConstraint(Exception ex)
    {
        System.out.println(ex.getMessage());
        publisher.publishEvent(new ServerErrorEvent(ex));
        return getResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ResponseEntity<Object> getResponseMessage(HttpStatus status, String message)
    {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

}
