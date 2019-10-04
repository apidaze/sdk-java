package com.apidaze.sdk.client.base;

import com.apidaze.sdk.client.credentials.Credentials;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.function.Function;

public abstract class BaseApiClient {

    private final static String API_SECRET = "api_secret";

    protected final static String BASE_URL = "https://api.apidaze.io";

    protected abstract String getBasePath();

    protected abstract Credentials getCredentials();

    protected Function<UriBuilder, UriBuilder> withAuthentication() {
        return uriBuilder -> uriBuilder
                .pathSegment(getCredentials().getApiKey())
                .pathSegment(getBasePath())
                .queryParam(API_SECRET, getCredentials().getApiSecret());
    }

    protected Function<UriBuilder, URI> uriWithAuthentication() {
        return withAuthentication().andThen(uriBuilder -> uriBuilder.build());
    }
}
