package com.apidaze.sdk.client.credentials;

import com.apidaze.sdk.client.base.BaseApiClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import static java.util.Objects.isNull;
import static org.springframework.util.Assert.notNull;

@AllArgsConstructor
public class CredentialsValidator extends BaseApiClient {

    private static final String BASE_PATH = "validates";

    private final WebClient client;
    private final Credentials credentials;

    @Builder
    public static CredentialsValidator create(@NotNull Credentials credentials, @Nullable String baseUrl) {
        notNull(credentials, "Credentials must not be null.");

        if (isNull(baseUrl)) {
            baseUrl = BASE_URL;
        }

        return new CredentialsValidator(WebClient.create(baseUrl), credentials);
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
