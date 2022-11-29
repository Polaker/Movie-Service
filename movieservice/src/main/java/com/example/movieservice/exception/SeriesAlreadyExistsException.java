package com.example.movieservice.exception;

public class SeriesAlreadyExistsException extends Exception {

    public SeriesAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
