package com.poliakov.recommendationservice.exception;

public class NoValuesException extends RuntimeException {

    private static final String MESSAGE = "No value exists for provided date range";

    public NoValuesException() {
        this(MESSAGE);
    }

    private NoValuesException(String message) {
        super(message);
    }

}
