package com.apidaze.sdk.client.externalscripts;

import org.mockserver.model.HttpRequest;

import static com.apidaze.sdk.client.AbstractClientTest.API_KEY;
import static com.apidaze.sdk.client.AbstractClientTest.API_SECRET;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpMethod.POST;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.Parameter.param;
import static org.mockserver.model.ParameterBody.params;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

class ExternalScriptsRequest {

    static HttpRequest getAll() {
        return request()
                .withMethod(GET.name())
                .withPath("/" + API_KEY + "/externalscripts")
                .withQueryStringParameters(param("api_secret", API_SECRET));
    }

    static HttpRequest getById(Long scriptId) {
        return request()
                .withMethod(GET.name())
                .withPath("/" + API_KEY + "/externalscripts/" + scriptId)
                .withQueryStringParameters(param("api_secret", API_SECRET));
    }

    static HttpRequest create(String scriptName, String scriptUrl) {
        return request()
                .withMethod(POST.name())
                .withHeader(CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                .withPath("/" + API_KEY + "/externalscripts")
                .withQueryStringParameters(param("api_secret", API_SECRET))
                .withBody(
                        params(
                                param("name", scriptName),
                                param("url", scriptUrl)
                        )
                );
    }
}
