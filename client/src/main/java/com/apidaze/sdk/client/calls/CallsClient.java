package com.apidaze.sdk.client.calls;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.credentials.Credentials;
import com.apidaze.sdk.client.messages.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import static java.util.Objects.isNull;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.util.Assert.notNull;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AllArgsConstructor(access = PRIVATE)
public class CallsClient extends BaseApiClient implements Calls {

    private static final String BASE_PATH = "calls";

    private final WebClient client;
    private final Credentials credentials;

    @Builder
    public static CallsClient create(@NotNull Credentials credentials, @Nullable String baseUrl) {
        notNull(credentials, "Credentials must not be null.");

        if (isNull(baseUrl)) {
            baseUrl = BASE_URL;
        }

        return new CallsClient(WebClient.create(baseUrl), credentials);
    }

    @Override
    public String create(PhoneNumber callerId, String origin, String destination, Type type) {
        return client.post()
                .uri(uriWithAuthentication())
                .body(fromFormData("callerid", callerId.getNumber())
                        .with("origin", origin)
                        .with("destination", destination)
                        .with("type", type.getValue()))
                .retrieve()
                .bodyToMono(CreateResponse.class)
                .map(CreateResponse::getId)
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

    @Data
    static class CreateResponse {
        @JsonProperty("ok")
        private String id;
    }
}
