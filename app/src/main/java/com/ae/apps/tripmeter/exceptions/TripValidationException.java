package com.ae.apps.tripmeter.exceptions;

/**
 * Exception that is thrown when validating data while creating or updating a trip
 */
public class TripValidationException extends Exception {

    public TripValidationException(String message) {
        super(message);
    }
}