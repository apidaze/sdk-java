package com.apidaze.sdk.client.messages;

import io.netty.handler.codec.http.HttpMethod;
import org.mockserver.model.HttpRequest;

import static com.apidaze.sdk.client.TestUtil.API_KEY;
import static com.apidaze.sdk.client.TestUtil.API_SECRET;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static org.mockserver.model.Parameter.param;
import static org.mockserver.model.ParameterBody.params;

class MessageRequest {

    static HttpRequest send(PhoneNumber from, PhoneNumber to, String body) {
        return HttpRequest.request()
                .withMethod(HttpMethod.POST.name())
                .withHeader(CONTENT_TYPE, "application/x-www-form-urlencoded")
                .withPath("/" + API_KEY + "/sms/send")
                .withQueryStringParameters(param("api_secret", API_SECRET))
                .withBody(params(
                        param("from", from.getNumber()),
                        param("to", to.getNumber()),
                        param("body", body)
                ));
    }
}
