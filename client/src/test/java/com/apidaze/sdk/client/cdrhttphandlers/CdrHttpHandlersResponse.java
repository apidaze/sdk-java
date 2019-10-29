package com.apidaze.sdk.client.cdrhttphandlers;

import org.mockserver.model.HttpResponse;

import java.util.List;

import static com.apidaze.sdk.client.TestUtil.APPLICATION_JSON_UTF8_VALUE;
import static com.apidaze.sdk.client.TestUtil.json;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.OK_200;

class CdrHttpHandlersResponse {

    static HttpResponse list(List<CdrHttpHandler> cdrHttpHandlers) {
        return response()
                .withStatusCode(OK_200.code())
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                .withBody(json(cdrHttpHandlers));
    }
}
