package com.apidaze.sdk.client.externalscripts;

import org.mockserver.model.HttpRequest;

import static com.apidaze.sdk.client.TestUtil.API_KEY;
import static com.apidaze.sdk.client.TestUtil.API_SECRET;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpMethod.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.Parameter.param;
import static org.mockserver.model.ParameterBody.params;

class ExternalScriptsRequest {

    private static final String BASE_PATH = "externalscripts";

    static HttpRequest getAll() {
        return request()
                .withMethod(GET.name())
                .withPath("/" + API_KEY + "/" + BASE_PATH)
                .withQueryStringParameters(param("api_secret", API_SECRET));
    }

    static HttpRequest getById(Long scriptId) {
        return request()
                .withMethod(GET.name())
                .withPath("/" + API_KEY + "/" + BASE_PATH + "/" + scriptId)
                .withQueryStringParameters(param("api_secret", API_SECRET));
    }

    static HttpRequest create(String scriptName, URL scriptUrl) {
        return request()
                .withMethod(POST.name())
                .withHeader(CONTENT_TYPE, "application/x-www-form-urlencoded")
                .withPath("/" + API_KEY + "/" + BASE_PATH)
                .withQueryStringParameters(param("api_secret", API_SECRET))
                .withBody(
                        params(
                                param("name", scriptName),
                                param("url", scriptUrl.getValue())
                        ));
    }

    static HttpRequest update(Long id, String newScriptName, URL newScriptUrl) {
        return request()
                .withMethod(PUT.name())
                .withHeader(CONTENT_TYPE, "application/x-www-form-urlencoded")
                .withPath("/" + API_KEY + "/" + BASE_PATH + "/" + id)
                .withQueryStringParameters(param("api_secret", API_SECRET))
                .withBody(
                        params(
                                param("name", newScriptName),
                                param("url", newScriptUrl.getValue())
                        ));
    }

    static HttpRequest updateUrl(Long id, URL newUrl) {
        return request()
                .withMethod(PUT.name())
                .withHeader(CONTENT_TYPE, "application/x-www-form-urlencoded")
                .withPath("/" + API_KEY + "/" + BASE_PATH + "/" + id)
                .withQueryStringParameters(param("api_secret", API_SECRET))
                .withBody(
                        params(
                                param("url", newUrl.getValue())
                        ));
    }

    static HttpRequest delete(Long id) {
        return request()
                .withMethod(DELETE.name())
                .withPath("/" + API_KEY + "/" + BASE_PATH + "/" + id)
                .withQueryStringParameters(param("api_secret", API_SECRET));
    }
}
