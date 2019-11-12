package com.apidaze.sdk.client.externalscripts;

import org.mockserver.model.HttpResponse;

import static com.apidaze.sdk.client.TestUtil.APPLICATION_JSON_UTF8_VALUE;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.ACCEPTED_202;

class ExternalScriptsResponse {

    static HttpResponse ok(String id) {
        return response()
                .withBody("{\"ok\" : \"" + id + "\"}")
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                .withStatusCode(ACCEPTED_202.code());
    }
}
