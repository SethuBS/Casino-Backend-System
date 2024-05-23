package com.casino.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid username")
public class PlayerUserNameNotFoundException extends RuntimeException {
    public PlayerUserNameNotFoundException(String message) {
        super(message);
    }
}
