package com.apidaze.sdk.client.recordings;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.credentials.Credentials;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
public class RecordingsClient extends BaseApiClient implements Recordings {

    private static final String BASE_PATH = "recordings";

    private final WebClient client;
    private final Credentials credentials;

    @Builder
    public static RecordingsClient create(@NotNull Credentials credentials) {
        Assert.notNull(credentials, "Credentials must not be null.");
        return new RecordingsClient(WebClient.create(BASE_URL), credentials);
    }

    @Override
    public List<String> list() {
        return client.get()
                .uri(uriWithAuthentication())
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
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
