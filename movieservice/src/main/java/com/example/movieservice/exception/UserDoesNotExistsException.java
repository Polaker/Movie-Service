package com.example.movieservice.exception;

public class UserDoesNotExistsException extends Exception {

    public UserDoesNotExistsException(String errorMessage) {
        super(errorMessage);
    }
}
