package com.apidaze.sdk.client.recordings;

import org.mockserver.model.HttpRequest;

import static com.apidaze.sdk.client.TestUtil.API_KEY;
import static com.apidaze.sdk.client.TestUtil.API_SECRET;
import static io.netty.handler.codec.http.HttpMethod.DELETE;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.Parameter.param;

class RecordingsClientRequest {

    private static final String BASE_PATH = "recordings";

    static HttpRequest getAll() {
        return request()
                .withMethod(GET.name())
                .withPath("/" + API_KEY + "/" + BASE_PATH)
                .withQueryStringParameters(param("api_secret", API_SECRET));
    }

    static HttpRequest download(String fileName) {
        return request()
                .withMethod(GET.name())
                .withPath("/" + API_KEY + "/" + BASE_PATH + "/" + fileName)
                .withQueryStringParameters(param("api_secret", API_SECRET));
    }

    static HttpRequest delete(String fileName) {
        return request()
                .withMethod(DELETE.name())
                .withPath("/" + API_KEY + "/" + BASE_PATH + "/" + fileName)
                .withQueryStringParameters(param("api_secret", API_SECRET));
    }
}
