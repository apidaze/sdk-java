package com.apidaze.sdk.client.recordings;

import com.apidaze.sdk.client.base.ApiClient;
import reactor.core.publisher.Flux;

public interface Recordings extends ApiClient {
    Flux<String> list();
}
