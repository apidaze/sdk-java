package com.apidaze.sdk.client.calls;

import org.mockserver.model.HttpResponse;

import java.util.List;

import static com.apidaze.sdk.client.TestUtil.json;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.ACCEPTED_202;
import static org.mockserver.model.HttpStatusCode.OK_200;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

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

    static HttpResponse list(List<ActiveCall> activeCalls) {
        return response()
                .withBody(json(activeCalls))
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                .withStatusCode(OK_200.code());
    }

    static HttpResponse one(ActiveCall activeCall) {
        return response()
                .withBody(json(activeCall))
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                .withStatusCode(OK_200.code());
    }
}