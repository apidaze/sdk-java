package com.apidaze.sdk.client.externalscripts;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExternalScripts {

    Flux<ExternalScript> list();

    Mono<ExternalScript> create(String name, String url);

    Mono<ExternalScript> get(Long id);

    Mono<ExternalScript> update(Long id, String name, String url);

    Mono<ExternalScript> updateName(Long id, String name);

    Mono<ExternalScript> updateUrl(Long id, String url);

    Mono<Void> delete(Long id);
}
