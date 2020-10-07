package com.ro.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRedirectException extends RuntimeException {
    public BadRedirectException(String message) {
        super(message);
    }
}
