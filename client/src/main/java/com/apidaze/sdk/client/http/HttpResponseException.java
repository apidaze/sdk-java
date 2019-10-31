package com.apidaze.sdk.client.http;

import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Optional;

@Getter
@ToString
public class HttpResponseException extends IOException {

    private final int statusCode;
    private final String statusMessage;
    private final Optional<String> responseBody;

    private HttpResponseException(String message, int statusCode, String statusMessage, @Nullable String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.responseBody = Optional.ofNullable(responseBody);
    }

    HttpResponseException(int statusCode, String statusMessage, @Nullable String responseBody) {
        this(statusCode + " " + statusMessage, statusCode, statusMessage, responseBody);
    }

    public static HttpResponseException create(int statusCode, String statusMessage, @Nullable String responseBody) {
        if (statusCode == 401) {
            return new Unauthorized(statusMessage, responseBody);
        } else {
            return new HttpResponseException(statusCode, statusMessage, responseBody);
        }
    }

    public static class Unauthorized extends HttpResponseException {

        Unauthorized(String statusMessage, @Nullable String responseBody) {
            super(401, statusMessage, responseBody);
        }
    }
}
