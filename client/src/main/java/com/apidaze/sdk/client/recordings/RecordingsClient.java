package com.apidaze.sdk.client.recordings;

import com.apidaze.sdk.client.credentials.Credentials;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.validation.constraints.NotNull;

import static com.apidaze.sdk.client.credentials.ApiAuthenticator.authenticate;

@AllArgsConstructor
public class RecordingsClient implements Recordings {

    private static final String BASE_PATH = "recordings";

    private final WebClient client;
    private final Credentials credentials;

    @Builder
    public static RecordingsClient create(@NotNull String baseUrl, @NotNull Credentials credentials) {
        Assert.notNull(baseUrl, "baseUrl must not be null");
        Assert.notNull(baseUrl, "credentials must not be null");

        return new RecordingsClient(WebClient.create(baseUrl), credentials);
    }

    @Override
    public Flux<String> list() {
        return client.get()
                .uri(authenticate(BASE_PATH, credentials)
                        .andThen(uriBuilder -> uriBuilder.build()))
                .retrieve()
                .bodyToFlux(String.class);
    }
}
