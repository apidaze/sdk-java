package com.apidaze.sdk.client.common;

public class InvalidPhoneNumberException extends RuntimeException {
    InvalidPhoneNumberException(String message) {
        super(message);
    }
}
