package com.apidaze.sdk.client.credentials;

import org.springframework.web.util.UriBuilder;

import java.util.function.Function;

public class ApiAuthenticator {

    private final static String API_SECRET = "api_secret";

    public static Function<UriBuilder, UriBuilder> authenticate(String basePath, Credentials credentials) {
        return uriBuilder -> uriBuilder
                .pathSegment(credentials.getApiKey())
                .pathSegment(basePath)
                .queryParam(API_SECRET, credentials.getApiSecret());
    }
}
