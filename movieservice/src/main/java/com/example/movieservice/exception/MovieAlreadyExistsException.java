package com.example.movieservice.exception;

public class MovieAlreadyExistsException extends Exception {

    public MovieAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
