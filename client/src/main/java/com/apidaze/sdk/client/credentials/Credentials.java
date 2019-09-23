package com.apidaze.sdk.client.credentials;

import lombok.Data;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;


@Data
public class Credentials {

    private final String apiKey;
    private final String apiSecret;

    public Credentials(@NotNull String apiKey, @NotNull String apiSecret) {
        Assert.notNull(apiKey, "apiKey must not be null");
        Assert.notNull(apiSecret, "apiSecret must not be null");

        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }
}