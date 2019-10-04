package com.apidaze.sdk.client.messages;

import io.netty.handler.codec.http.HttpMethod;
import org.mockserver.model.HttpRequest;

import static com.apidaze.sdk.client.TestUtil.API_KEY;
import static com.apidaze.sdk.client.TestUtil.API_SECRET;
import static org.mockserver.model.Parameter.param;
import static org.mockserver.model.ParameterBody.params;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

public class MessageRequest {

    static HttpRequest send(PhoneNumber from, PhoneNumber to, String body) {
        return HttpRequest.request()
                .withMethod(HttpMethod.POST.name())
                .withHeader(CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                .withPath("/" + API_KEY + "/sms")
                .withQueryStringParameters(param("api_secret", API_SECRET))
                .withBody(params(
                        param("from", from.getNumber()),
                        param("to", to.getNumber()),
                        param("body", body)
                ));
    }
}
