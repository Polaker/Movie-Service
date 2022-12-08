package com.example.movieservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class SubscriptionAlreadyBoughtException extends Exception {

    public SubscriptionAlreadyBoughtException(String errorMessage) {
        super(errorMessage);
    }
}
