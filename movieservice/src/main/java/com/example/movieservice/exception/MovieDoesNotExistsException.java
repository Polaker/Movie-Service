package com.example.movieservice.exception;

public class MovieDoesNotExistsException extends Exception {

    public MovieDoesNotExistsException(String errorMessage) {
        super(errorMessage);
    }
}
