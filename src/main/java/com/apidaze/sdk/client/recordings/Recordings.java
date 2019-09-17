package com.apidaze.sdk.client.recordings;

import reactor.core.publisher.Flux;

public interface Recordings {
    Flux<String> list();
}
