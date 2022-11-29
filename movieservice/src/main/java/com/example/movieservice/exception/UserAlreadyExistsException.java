package com.example.movieservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends Exception {

    public UserAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
