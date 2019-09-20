package com.apidaze.sdk.client.recordings;

import com.apidaze.sdk.client.credentials.Credentials;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.validation.constraints.NotNull;

import static com.apidaze.sdk.client.base.ApiAuthenticator.uriWithAuthentication;

@AllArgsConstructor
public class RecordingsClient implements Recordings {

    private static final String BASE_PATH = "recordings";

    private final WebClient client;
    private final Credentials credentials;

    @Builder
    public static RecordingsClient create(@NotNull Credentials credentials) {
        Assert.notNull(credentials, "Credentials must not be null.");
        return new RecordingsClient(WebClient.create(BASE_URL), credentials);
    }

    @Override
    public Flux<String> list() {
        return client.get()
                .uri(uriWithAuthentication(BASE_PATH, credentials))
                .retrieve()
                .bodyToFlux(String.class);
    }
}
