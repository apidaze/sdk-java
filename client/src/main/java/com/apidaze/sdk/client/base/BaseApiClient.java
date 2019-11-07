package com.apidaze.sdk.client.base;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.HttpUrl;


public abstract class BaseApiClient {

    protected final static String DEFAULT_BASE_URL = "https://api.apidaze.io";

    protected final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .registerModule(new Jdk8Module())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    protected abstract String getBasePath();

    protected abstract String getBaseUrl();

    protected abstract Credentials getCredentials();

    protected HttpUrl authenticatedUrl() {
        return authenticated().build();
    }

    protected HttpUrl.Builder authenticated() {
        return HttpUrl
                .parse(getBaseUrl())
                .newBuilder()
                .addPathSegments(getCredentials().getApiKey())
                .addPathSegment(getBasePath())
                .addQueryParameter("api_secret", getCredentials().getApiSecret());
    }
}
