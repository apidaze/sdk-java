package com.apidaze.sdk.client.calls;

import com.apidaze.sdk.client.common.PhoneNumber;
import org.mockserver.model.HttpRequest;

import java.util.UUID;

import static com.apidaze.sdk.client.TestUtil.API_KEY;
import static com.apidaze.sdk.client.TestUtil.API_SECRET;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpMethod.DELETE;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.Parameter.param;
import static org.mockserver.model.ParameterBody.params;

class CallsRequest {

    private static final String BASE_PATH = "calls";

    static HttpRequest create(PhoneNumber callerId, String origin, String destination, Calls.CallType callType) {
        return request()
                .withMethod("POST")
                .withHeader(CONTENT_TYPE, "application/x-www-form-urlencoded")
                .withPath("/" + API_KEY + "/" + BASE_PATH)
                .withQueryStringParameters(param("api_secret", API_SECRET))
                .withBody(
                        params(
                                param("callerid", callerId.getNumber()),
                                param("origin", origin),
                                param("destination", destination),
                                param("type", callType.getValue())
                        ));
    }

    static HttpRequest getActiveCalls() {
        return request()
                .withMethod(GET.name())
                .withPath("/" + API_KEY + "/" + BASE_PATH)
                .withQueryStringParameters(param("api_secret", API_SECRET));
    }

    static HttpRequest getActiveCall(UUID id) {
        return request()
                .withMethod(GET.name())
                .withPath("/" + API_KEY + "/" + BASE_PATH + "/" + id)
                .withQueryStringParameters(param("api_secret", API_SECRET));
    }

    static HttpRequest delete(UUID id) {
        return request()
                .withMethod(DELETE.name())
                .withPath("/" + API_KEY + "/" + BASE_PATH + "/" + id)
                .withQueryStringParameters(param("api_secret", API_SECRET));
    }
}
