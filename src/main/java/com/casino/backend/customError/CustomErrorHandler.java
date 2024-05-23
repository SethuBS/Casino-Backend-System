package com.casino.backend.customError;

import com.casino.backend.exception.InsufficientBalanceException;
import com.casino.backend.exception.InvalidTransactionException;
import com.casino.backend.exception.PlayerNotFoundException;
import com.casino.backend.exception.PlayerUserNameNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomErrorHandler {

    private void getServletRequestAttributesAndSetPath(ErrorDetail error) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String path = request.getRequestURI();
            error.setPath(path);
        }
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorDetail> handleInsufficientBalanceException(InsufficientBalanceException e) {
        ErrorDetail error = new ErrorDetail();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.I_AM_A_TEAPOT.value());
        error.setError(HttpStatus.I_AM_A_TEAPOT.name());
        error.setReason(e.getMessage());
        getServletRequestAttributesAndSetPath(error);
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(error);
    }

    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<ErrorDetail> handleInvalidTransactionException(InvalidTransactionException e) {
        ErrorDetail error = new ErrorDetail();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError(HttpStatus.BAD_REQUEST.name());
        error.setReason(e.getMessage());
        getServletRequestAttributesAndSetPath(error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<ErrorDetail> handlePlayerNotFoundException(PlayerNotFoundException e) {
        ErrorDetail error = new ErrorDetail();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError(HttpStatus.BAD_REQUEST.name());
        error.setReason(e.getMessage());
        getServletRequestAttributesAndSetPath(error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(PlayerUserNameNotFoundException.class)
    public ResponseEntity<ErrorDetail> handlePlayerUserNameNotFoundException(PlayerUserNameNotFoundException e) {
        ErrorDetail error = new ErrorDetail();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError(HttpStatus.BAD_REQUEST.name());
        error.setReason(e.getMessage());
        getServletRequestAttributesAndSetPath(error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
