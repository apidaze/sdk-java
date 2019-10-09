package com.apidaze.sdk.client.externalscripts;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.credentials.Credentials;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import java.util.List;

import static java.util.Objects.isNull;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AllArgsConstructor
public class ExternalScriptsClient extends BaseApiClient implements ExternalScripts {

    public static final int MAX_NAME_LENGTH = 40;

    private static final String BASE_PATH = "externalscripts";
    private static final String URL = "url";
    private static final String NAME = "name";

    private final WebClient client;
    private final Credentials credentials;

    @Builder
    public static ExternalScriptsClient create(@NotNull Credentials credentials, @Nullable String baseUrl) {
        Assert.notNull(credentials, "Credentials must not be null.");

        if (isNull(baseUrl)) {
            baseUrl = BASE_URL;
        }

        return new ExternalScriptsClient(WebClient.create(baseUrl), credentials);
    }

    @Override
    public List<ExternalScript> list() {
        return client.get()
                .uri(uriWithAuthentication())
                .retrieve()
                .bodyToFlux(ExternalScript.class)
                .collectList()
                .block();
    }

    @Override
    public ExternalScript create(String name, URL url) {
        validateName(name);

        return client.post()
                .uri(uriWithAuthentication())
                .body(fromFormData(NAME, name).with(URL, url.getValue()))
                .retrieve()
                .bodyToMono(ExternalScript.class)
                .block();
    }

    @Override
    public ExternalScript get(Long id) {
        Assert.notNull(id, "id must not be null");

        return client.get()
                .uri(withAuthentication().andThen(builder -> builder.pathSegment(String.valueOf(id)).build()))
                .retrieve()
                .bodyToMono(ExternalScript.class)
                .block();
    }

    @Override
    public ExternalScript update(Long id, String name, URL url) {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(url, "url must not be null");
        validateName(name);

        return client.put()
                .uri(withAuthentication().andThen(builder -> builder.pathSegment(String.valueOf(id)).build()))
                .body(fromFormData(NAME, name).with(URL, url.getValue()))
                .retrieve()
                .bodyToMono(ExternalScript.class)
                .block();
    }

    @Override
    public ExternalScript updateUrl(Long id, URL url) {
        Assert.notNull(id, "id must not be null");

        return client.put()
                .uri(withAuthentication().andThen(builder -> builder.pathSegment(String.valueOf(id)).build()))
                .body(fromFormData(URL, url.getValue()))
                .retrieve()
                .bodyToMono(ExternalScript.class)
                .block();
    }


    @Override
    public Void delete(Long id) {
        Assert.notNull(id, "id must not be null");

        return client.delete()
                .uri(withAuthentication().andThen(builder -> builder.pathSegment(String.valueOf(id)).build()))
                .retrieve()
                .bodyToMono(Void.class)
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

    private static void validateName(String name) {
        Assert.notNull(name, "name must not be null");
        Assert.isTrue(name.length() <= MAX_NAME_LENGTH, "name: maximum " + MAX_NAME_LENGTH + " characters long");
    }
}
