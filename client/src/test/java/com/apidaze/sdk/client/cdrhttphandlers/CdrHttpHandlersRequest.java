package com.apidaze.sdk.client.cdrhttphandlers;

import org.mockserver.model.HttpRequest;

import static com.apidaze.sdk.client.TestUtil.API_KEY;
import static com.apidaze.sdk.client.TestUtil.API_SECRET;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.Parameter.param;

class CdrHttpHandlersRequest {

    private static final String BASE_PATH = "cdrhttphandlers";

    static HttpRequest getAll() {
        return request()
                .withMethod(GET.name())
                .withPath("/" + API_KEY + "/" + BASE_PATH)
                .withQueryStringParameters(param("api_secret", API_SECRET));
    }

}
