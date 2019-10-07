package com.apidaze.sdk.client.calls;

import com.apidaze.sdk.client.messages.PhoneNumber;
import org.mockserver.model.HttpRequest;

import static com.apidaze.sdk.client.TestUtil.API_KEY;
import static com.apidaze.sdk.client.TestUtil.API_SECRET;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.Parameter.param;
import static org.mockserver.model.ParameterBody.params;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.POST;

class CallsRequest {

    static HttpRequest create(PhoneNumber callerId, String origin, String destination, Calls.Type type) {
        return request()
                .withMethod(POST.name())
                .withHeader(CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                .withPath("/" + API_KEY + "/calls")
                .withQueryStringParameters(param("api_secret", API_SECRET))
                .withBody(
                        params(
                                param("callerid", callerId.getNumber()),
                                param("origin", origin),
                                param("destination", destination),
                                param("type", type.getValue())
                        ));
    }
}
