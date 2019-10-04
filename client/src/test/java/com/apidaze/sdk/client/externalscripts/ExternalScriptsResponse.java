package com.apidaze.sdk.client.externalscripts;

import org.mockserver.model.HttpResponse;

import java.util.List;

import static com.apidaze.sdk.client.TestUtil.json;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

class ExternalScriptsResponse {

    static HttpResponse one(ExternalScript externalScript) {
        return response()
                .withStatusCode(OK.value())
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                .withBody(json(externalScript));
    }

    static HttpResponse list(List<ExternalScript> externalScripts) {
        return response()
                .withStatusCode(OK.value())
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                .withBody(json(externalScripts));
    }
}
