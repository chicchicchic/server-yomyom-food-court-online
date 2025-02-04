package com.jkgroup.foodCourtServerSide.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends RuntimeException {
    private List<String> errors;


    public ValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
