package com.apidaze.sdk.client.base;

import lombok.Data;

import static java.util.Objects.requireNonNull;

/**
 * The credentials to use in authenticate in Apidaze REST API
 */
@Data
public class Credentials {

    private final String apiKey;
    private final String apiSecret;

    public Credentials(String apiKey, String apiSecret) {
        requireNonNull(apiKey, "apiKey must not be null");
        requireNonNull(apiSecret, "apiSecret must not be null");

        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }
}