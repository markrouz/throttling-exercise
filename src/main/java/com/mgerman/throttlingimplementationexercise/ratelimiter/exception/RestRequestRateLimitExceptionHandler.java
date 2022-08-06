package com.mgerman.throttlingimplementationexercise.ratelimiter.exception;

import com.mgerman.throttlingimplementationexercise.ratelimiter.exception.RequestRateLimitException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestRequestRateLimitExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {RequestRateLimitException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.BAD_GATEWAY, request);
    }

}
