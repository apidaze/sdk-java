package com.apidaze.sdk.client.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.val;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

class HttpErrorHandler implements Interceptor {

    private static final String UNAUTHORIZED_MESSAGE_404 = "api secret and api key are not a pair";

    private final ObjectMapper mapper =
            new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        val response = chain.proceed(chain.request());

        if (!response.isSuccessful()) {
            val code = response.code();
            val body = response.body().string();

            if (code == 404 && containsUnauthorized404Message(body)) { // WA for the incorrect error code returned by API
                throw new HttpResponseException.Unauthorized("Unauthorized", body);
            } else {
                throw HttpResponseException.create(code, response.message(), body);
            }
        }

        return response;
    }

    private boolean containsUnauthorized404Message(String responseBody) {
        try {
            val response = mapper.readValue(responseBody, ErrorResponse.class);
            return UNAUTHORIZED_MESSAGE_404.equals(response.message);
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    @Data
    private static class ErrorResponse {
        private String code;
        private String message;
    }
}
