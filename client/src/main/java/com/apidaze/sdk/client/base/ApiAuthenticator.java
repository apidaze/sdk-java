package com.apidaze.sdk.client.base;

import com.apidaze.sdk.client.credentials.Credentials;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.function.Function;

public class ApiAuthenticator {

    private final static String API_SECRET = "api_secret";

    public static Function<UriBuilder, UriBuilder> withAuthentication(String basePath, Credentials credentials) {
        return uriBuilder -> uriBuilder
                .pathSegment(credentials.getApiKey())
                .pathSegment(basePath)
                .queryParam(API_SECRET, credentials.getApiSecret());
    }

    public static Function<UriBuilder, URI> uriWithAuthentication(String basePath, Credentials credentials) {
        return withAuthentication(basePath, credentials)
                .andThen(uriBuilder -> uriBuilder.build());
    }
}
