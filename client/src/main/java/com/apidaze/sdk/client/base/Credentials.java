package com.apidaze.sdk.client.base;

import lombok.Data;
import javax.validation.constraints.NotNull;
import static org.springframework.util.Assert.notNull;

@Data
public class Credentials {

    private final String apiKey;
    private final String apiSecret;

    public Credentials(@NotNull String apiKey, @NotNull String apiSecret) {
        notNull(apiKey, "apiKey must not be null");
        notNull(apiSecret, "apiSecret must not be null");

        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }
}