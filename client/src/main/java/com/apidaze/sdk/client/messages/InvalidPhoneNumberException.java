package com.apidaze.sdk.client.messages;

public class InvalidPhoneNumberException extends RuntimeException {
    InvalidPhoneNumberException(String message) {
        super(message);
    }
}
