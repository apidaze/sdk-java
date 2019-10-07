package com.apidaze.sdk.client.calls;

import org.mockserver.model.HttpResponse;

import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.ACCEPTED_202;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

class CallsResponse {

    static HttpResponse ok(String callId) {
        return response()
                .withBody("{\"ok\" : \"" + callId + "\"}")
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                .withStatusCode(ACCEPTED_202.code());
    }
}