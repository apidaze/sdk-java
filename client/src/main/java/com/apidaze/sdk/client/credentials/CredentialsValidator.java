package com.apidaze.sdk.client.credentials;

import com.apidaze.sdk.client.base.BaseApiClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
public class CredentialsValidator extends BaseApiClient {

    private static final String BASE_PATH = "validates";

    private final WebClient client;
    private final Credentials credentials;

    @Builder
    public static CredentialsValidator create(@NotNull Credentials credentials) {
        Assert.notNull(credentials, "Credentials must not be null.");

        return new CredentialsValidator(WebClient.create(BASE_URL), credentials);
    }

    public String validateCredentials() {
        return client.get()
                .uri(uriWithAuthentication())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    protected String getBasePath() {
        return BASE_PATH;
    }

    @Override
    protected Credentials getCredentials() {
        return credentials;
    }
}
