package com.apidaze.sdk.client.common;

/**
 * An exception thrown when an error occurs during creating {@link PhoneNumber} instance.
 */
public class InvalidPhoneNumberException extends RuntimeException {
    InvalidPhoneNumberException(String message) {
        super(message);
    }
}
