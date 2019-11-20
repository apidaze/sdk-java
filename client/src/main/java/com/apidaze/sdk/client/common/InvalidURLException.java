package com.apidaze.sdk.client.common;

/**
 * An exception thrown when an error occurs during creating {@link URL} instance.
 */
public class InvalidURLException extends RuntimeException {
    InvalidURLException(String msg) {
        super(msg);
    }
}
