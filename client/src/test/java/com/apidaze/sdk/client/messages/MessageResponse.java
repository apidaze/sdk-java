package com.apidaze.sdk.client.messages;

import org.mockserver.model.HttpResponse;

import static com.apidaze.sdk.client.TestUtil.APPLICATION_JSON_UTF8_VALUE;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static org.mockserver.model.HttpStatusCode.OK_200;


class MessageResponse {

    static HttpResponse ok(String body) {
        return HttpResponse.response()
                .withStatusCode(OK_200.code())
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                .withBody(body);
    }
}
