package com.apidaze.sdk.client.messages;

import org.mockserver.model.HttpResponse;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

public class MessageResponse {

    static HttpResponse ok(String body) {
        return HttpResponse.response()
                .withStatusCode(HttpStatus.OK.value())
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                .withBody(body);
    }
}
