package com.apidaze.sdk.client.externalscripts;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExternalScripts {

    Flux<ExternalScript> list();

    Mono<ExternalScript> create(String name, URL url);

    Mono<ExternalScript> get(Long id);

    Mono<ExternalScript> update(Long id, String name, URL url);

    Mono<ExternalScript> updateUrl(Long id, URL url);

    Mono<Void> delete(Long id);
}
