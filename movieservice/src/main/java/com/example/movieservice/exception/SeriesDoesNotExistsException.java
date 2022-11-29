package com.example.movieservice.exception;

public class SeriesDoesNotExistsException extends Exception {

    public SeriesDoesNotExistsException(String errorMessage) {
        super(errorMessage);
    }
}
