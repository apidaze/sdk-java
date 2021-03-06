package com.apidaze.sdk.client.http;

import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Optional;

/**
 * Exception that contain actual HTTP response data.
 * It is thrown if REST API returned an unhappy HTTP response code like 404 or 500
 */
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
        switch (statusCode) {
            case 401:
                return new Unauthorized(statusMessage, responseBody);
            case 404:
                return new NotFound(statusMessage, responseBody);
            default:
                return new HttpResponseException(statusCode, statusMessage, responseBody);
        }
    }

    public static class Unauthorized extends HttpResponseException {

        Unauthorized(String statusMessage, @Nullable String responseBody) {
            super(401, statusMessage, responseBody);
        }
    }

    public static class NotFound extends HttpResponseException {

        NotFound(String statusMessage, @Nullable String responseBody) {
            super(404, statusMessage, responseBody);
        }
    }
}
