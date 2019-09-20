package com.apidaze.sdk.client.externalscripts;

import com.apidaze.sdk.client.credentials.Credentials;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

import static com.apidaze.sdk.client.base.ApiAuthenticator.withAuthentication;
import static com.apidaze.sdk.client.base.ApiAuthenticator.uriWithAuthentication;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AllArgsConstructor
public class ExternalScriptsClient implements ExternalScripts {

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
                .uri(uriWithAuthentication(BASE_PATH, credentials))
                .retrieve()
                .bodyToFlux(ExternalScript.class);
    }

    @Override
    public Mono<ExternalScript> create(String name, String url) {
        return client.post()
                .uri(uriWithAuthentication(BASE_PATH, credentials))
                .body(fromFormData(NAME, name).with(URL, url))
                .retrieve()
                .bodyToMono(ExternalScript.class);
    }

    @Override
    public Mono<ExternalScript> get(Long id) {
        return client.get()
                .uri(withAuthentication(BASE_PATH, credentials)
                        .andThen(builder -> builder.pathSegment(id.toString()).build()))
                .retrieve()
                .bodyToMono(ExternalScript.class);
    }

    @Override
    public Mono<ExternalScript> update(Long id, String name, String url) {
        return client.put()
                .uri(withAuthentication(BASE_PATH, credentials)
                        .andThen(builder -> builder.pathSegment(id.toString()).build()))
                .body(fromFormData(NAME, name).with(URL, url))
                .retrieve()
                .bodyToMono(ExternalScript.class);
    }

    @Override
    public Mono<ExternalScript> updateName(Long id, String name) {
        return client.put()
                .uri(withAuthentication(BASE_PATH, credentials)
                        .andThen(builder -> builder.pathSegment(id.toString()).build()))
                .body(fromFormData(NAME, name))
                .retrieve()
                .bodyToMono(ExternalScript.class);
    }

    @Override
    public Mono<ExternalScript> updateUrl(Long id, String url) {
        return client.put()
                .uri(withAuthentication(BASE_PATH, credentials)
                        .andThen(builder -> builder.pathSegment(id.toString()).build()))
                .body(fromFormData(URL, url))
                .retrieve()
                .bodyToMono(ExternalScript.class);
    }


    @Override
    public Mono<Void> delete(Long id) {
        return client.delete()
                .uri(withAuthentication(BASE_PATH, credentials)
                        .andThen(builder -> builder.pathSegment(id.toString()).build()))
                .retrieve()
                .bodyToMono(Void.class);
    }
}
