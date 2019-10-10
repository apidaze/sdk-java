package com.apidaze.sdk.client.validates;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.base.Credentials;
import lombok.AllArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.validation.constraints.NotNull;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.util.Assert.notNull;

@AllArgsConstructor(access = PRIVATE)
public class CredentialsValidator extends BaseApiClient {

    private static final String BASE_PATH = "validates";

    private final WebClient client;
    private final Credentials credentials;

    public static CredentialsValidator create(@NotNull Credentials credentials) {
        return create(credentials, BASE_URL);
    }

    static CredentialsValidator create(@NotNull Credentials credentials, @NotNull String baseUrl) {
        notNull(credentials, "Credentials must not be null.");
        notNull(baseUrl, "baseUrl must not be null.");

        return new CredentialsValidator(WebClient.create(baseUrl), credentials);
    }

    public boolean validateCredentials() {
        try {
            client.get()
                    .uri(uriWithAuthentication())
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            return true;
        } catch (WebClientResponseException.NotFound | WebClientResponseException.Unauthorized e) {
            return false;
        }
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
