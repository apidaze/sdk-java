package com.apidaze.sdk.client.externalscripts;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.credentials.Credentials;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AllArgsConstructor
public class ExternalScriptsClient extends BaseApiClient implements ExternalScripts {

    private static final String BASE_PATH = "externalscripts";
    private static final String URL = "url";
    private static final String NAME = "name";

    private final WebClient client;
    private final Credentials credentials;

    @Builder
    public static ExternalScriptsClient create(@NotNull Credentials credentials) {
        Assert.notNull(credentials, "Credentials must not be null.");
        return new ExternalScriptsClient(WebClient.create(BASE_URL), credentials);
    }

    @Override
    public Flux<ExternalScript> list() {
        return client.get()
                .uri(uriWithAuthentication())
                .retrieve()
                .bodyToFlux(ExternalScript.class);
    }

    @Override
    public Mono<ExternalScript> create(String name, String url) {
        return client.post()
                .uri(uriWithAuthentication())
                .body(fromFormData(NAME, name).with(URL, url))
                .retrieve()
                .bodyToMono(ExternalScript.class);
    }

    @Override
    public Mono<ExternalScript> get(@NotNull Long id) {
        Assert.notNull(id, "id must not be null");
        return client.get()
                .uri(withAuthentication().andThen(builder -> builder.pathSegment(String.valueOf(id)).build()))
                .retrieve()
                .bodyToMono(ExternalScript.class);
    }

    @Override
    public Mono<ExternalScript> update(@NotNull Long id, String name, String url) {
        Assert.notNull(id, "id must not be null");
        return client.put()
                .uri(withAuthentication().andThen(builder -> builder.pathSegment(String.valueOf(id)).build()))
                .body(fromFormData(NAME, name).with(URL, url))
                .retrieve()
                .bodyToMono(ExternalScript.class);
    }

    @Override
    public Mono<ExternalScript> updateName(@NotNull Long id, String name) {
        Assert.notNull(id, "id must not be null");
        return client.put()
                .uri(withAuthentication().andThen(builder -> builder.pathSegment(String.valueOf(id)).build()))
                .body(fromFormData(NAME, name))
                .retrieve()
                .bodyToMono(ExternalScript.class);
    }

    @Override
    public Mono<ExternalScript> updateUrl(@NotNull Long id, String url) {
        return client.put()
                .uri(withAuthentication().andThen(builder -> builder.pathSegment(String.valueOf(id)).build()))
                .body(fromFormData(URL, url))
                .retrieve()
                .bodyToMono(ExternalScript.class);
    }


    @Override
    public Mono<Void> delete(@NotNull Long id) {
        Assert.notNull(id, "id must not be null");
        return client.delete()
                .uri(withAuthentication().andThen(builder -> builder.pathSegment(String.valueOf(id)).build()))
                .retrieve()
                .bodyToMono(Void.class);
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
