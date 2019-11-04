package com.apidaze.sdk.client.calls;

import org.mockserver.model.HttpResponse;

import static com.apidaze.sdk.client.TestUtil.APPLICATION_JSON_UTF8_VALUE;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.ACCEPTED_202;

class CallsResponse {

    static HttpResponse ok(String callId) {
        return response()
                .withBody("{\"ok\" : \"" + callId + "\"}")
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                .withStatusCode(ACCEPTED_202.code());
    }

    static HttpResponse failed(String message) {
        return response()
                .withBody("{\"failure\" : \"" + message + "\"}")
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                .withStatusCode(ACCEPTED_202.code());
    }

    static HttpResponse emptyJson() {
        return response()
                .withBody("{}")
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                .withStatusCode(ACCEPTED_202.code());
    }

}