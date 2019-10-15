package com.apidaze.sdk.client.base;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import static java.time.Duration.ofSeconds;


public abstract class BaseApiClient {

    protected final static String DEFAULT_BASE_URL = "https://api.apidaze.io";
    protected final static long CONNECTION_TIMEOUT_SECONDS = 10;
    protected final static long READ_TIMEOUT_SECONDS = 30;
    protected final static long WRITE_TIMEOUT_SECONDS = 30;
    protected final static long CALL_TIMEOUT_SECONDS = 60;

    protected final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .registerModule(new Jdk8Module())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    protected final static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(ofSeconds(CONNECTION_TIMEOUT_SECONDS))
            .readTimeout(ofSeconds(READ_TIMEOUT_SECONDS))
            .writeTimeout(ofSeconds(WRITE_TIMEOUT_SECONDS))
            .callTimeout(ofSeconds(CALL_TIMEOUT_SECONDS))
            .build();

    protected abstract String getBasePath();

    protected abstract String getBaseUrl();

    protected abstract Credentials getCredentials();

    protected HttpUrl authenticatedUrl() {
        return authenticated().build();
    }

    protected HttpUrl.Builder authenticated() {
        return HttpUrl.parse(getBaseUrl())
                .newBuilder()
                .addPathSegments(getCredentials().getApiKey())
                .addPathSegment(getBasePath())
                .addQueryParameter("api_secret", getCredentials().getApiSecret());
    }
}
