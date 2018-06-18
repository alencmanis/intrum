package com.intrum.lenc.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnautorizedException extends RuntimeException {
    public UnautorizedException() {
        super();
    }
    public UnautorizedException(String message, Throwable cause) {
        super(message, cause);
    }
    public UnautorizedException(String message) {
        super(message);
    }
    public UnautorizedException(Throwable cause) {
        super(cause);
    }
}