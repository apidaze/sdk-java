package com.apidaze.sdk.client.calls;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.messages.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;
import org.springframework.web.reactive.function.client.WebClient;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.util.Assert.hasLength;
import static org.springframework.util.Assert.notNull;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AllArgsConstructor(access = PRIVATE)
public class CallsClient extends BaseApiClient implements Calls {

    private static final String BASE_PATH = "calls";

    private final WebClient client;
    private final Credentials credentials;

    public static CallsClient create(@NotNull Credentials credentials) {
        return create(credentials, BASE_URL);
    }

    static CallsClient create(@NotNull Credentials credentials, @NotNull String baseUrl) {
        notNull(credentials, "Credentials must not be null.");
        notNull(baseUrl, "baseUrl must not be null.");

        return new CallsClient(WebClient.create(baseUrl), credentials);
    }

    @Override
    public UUID create(PhoneNumber callerId, String origin, String destination, Type callType) {
        notNull(callerId, "callerId must not be null");
        notNull(callType, "type must not be null");
        hasLength(origin, "origin must not be empty");
        hasLength(destination, "destination must not be empty");

        val response = client.post()
                .uri(uriWithAuthentication())
                .body(fromFormData("callerid", callerId.getNumber())
                        .with("origin", origin)
                        .with("destination", destination)
                        .with("type", callType.getValue()))
                .retrieve()
                .bodyToMono(GenericResponse.class)
                .block();

        return requireNonNull(response, "API returned empty response body")
                .getOk()
                .map(UUID::fromString)
                .orElseThrow(() -> new ApiResponseException(
                        response.getFailure().orElse("missing call id in the response body")));
    }

    @Override
    public List<ActiveCall> getActiveCalls() {
        return client.get()
                .uri(uriWithAuthentication())
                .retrieve()
                .bodyToFlux(ActiveCall.class)
                .collectList()
                .block();
    }

    @Override
    public ActiveCall getActiveCall(UUID id) {
        notNull(id, "id must no be null");

        return client.get()
                .uri(withAuthentication().andThen(uriBuilder ->
                        uriBuilder.pathSegment(id.toString()).build()))
                .retrieve()
                .bodyToMono(ActiveCall.class)
                .block();
    }

    @Override
    public void deleteActiveCall(UUID id) {
        val response = client.delete()
                .uri(withAuthentication().andThen(uriBuilder ->
                        uriBuilder.pathSegment(id.toString()).build()))
                .retrieve()
                .bodyToMono(GenericResponse.class)
                .block();

        if (response != null) {
            response.getFailure().ifPresent(message -> {
                throw new ApiResponseException(message);
            });
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

    public static class ApiResponseException extends RuntimeException {
        ApiResponseException(String message) {
            super(message);
        }
    }

    @Data
    private static class GenericResponse {
        private Optional<String> ok = Optional.empty();
        private Optional<String> failure = Optional.empty();
    }
}
