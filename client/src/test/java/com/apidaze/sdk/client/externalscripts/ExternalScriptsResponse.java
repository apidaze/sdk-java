package com.apidaze.sdk.client.externalscripts;

import org.mockserver.model.HttpResponse;

import java.util.List;

import static com.apidaze.sdk.client.TestUtil.APPLICATION_JSON_UTF8_VALUE;
import static com.apidaze.sdk.client.TestUtil.json;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.ACCEPTED_202;
import static org.mockserver.model.HttpStatusCode.OK_200;

class ExternalScriptsResponse {

    static HttpResponse one(ExternalScript externalScript) {
        return response()
                .withStatusCode(OK_200.code())
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                .withBody(json(externalScript));
    }

    static HttpResponse list(List<ExternalScript> externalScripts) {
        return response()
                .withStatusCode(OK_200.code())
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                .withBody(json(externalScripts));
    }

    static HttpResponse ok(String id) {
        return response()
                .withBody("{\"ok\" : \"" + id + "\"}")
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                .withStatusCode(ACCEPTED_202.code());
    }
}
