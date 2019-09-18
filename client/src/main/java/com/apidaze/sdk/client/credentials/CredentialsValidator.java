package com.apidaze.sdk.client.credentials;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

import static com.apidaze.sdk.client.credentials.ApiAuthenticator.authenticate;

@AllArgsConstructor
public class CredentialsValidator {

    private static final String BASE_PATH = "validates";

    private final WebClient client;
    private final Credentials credentials;

    @Builder
    public static CredentialsValidator create(@NotNull String baseUrl, @NotNull Credentials credentials) {
        Assert.notNull(baseUrl, "baseUrl must not be null");
        Assert.notNull(baseUrl, "credentials must not be null");

        return new CredentialsValidator(WebClient.create(baseUrl), credentials);
    }

    public Mono<String> validateCredentials() {
        return client.get()
                .uri(authenticate(BASE_PATH, credentials)
                        .andThen(uriBuilder -> uriBuilder.build()))
                .retrieve()
                .bodyToMono(String.class);
    }

}
