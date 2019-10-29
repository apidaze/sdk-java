package com.apidaze.sdk.client.calls;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.common.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.val;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class CallsClient extends BaseApiClient<ActiveCall> implements Calls {

    @Getter
    private final String basePath = "calls";
    @Getter
    private final Credentials credentials;
    @Getter
    private final String baseUrl;

    public static CallsClient create(Credentials credentials) {
        return create(credentials, DEFAULT_BASE_URL);
    }

    static CallsClient create(Credentials credentials, String baseUrl) {
        requireNonNull(credentials, "Credentials must not be null.");
        requireNonNull(baseUrl, "baseUrl must not be null.");

        return new CallsClient(credentials, baseUrl);
    }

    @Override
    public UUID createCall(PhoneNumber callerId, String origin, String destination, CallType callType) throws IOException {
        requireNonNull(callerId, "callerId must not be null");
        requireNonNull(callType, "type must not be null");

        if (isNullOrEmpty(origin)) throw new IllegalArgumentException("origin must not be null or empty");
        if (isNullOrEmpty(destination)) throw new IllegalArgumentException("destination must not be null or empty");

        RequestBody formBody = new FormBody.Builder()
                .add("callerid", callerId.getNumber())
                .add("origin", origin)
                .add("destination", destination)
                .add("type", callType.getValue())
                .build();

        Request request = new Request.Builder()
                .url(authenticatedUrl())
                .post(formBody)
                .build();


        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            val responseBody = mapper.readValue(response.body().string(), GenericResponse.class);

            return requireNonNull(responseBody, "API returned empty response body")
                    .getOk()
                    .map(UUID::fromString)
                    .orElseThrow(() -> new CreateResponseException(
                            responseBody.getFailure().orElse("missing call id in the response body")));
        }
    }

    @Override
    public List<ActiveCall> getActiveCalls() throws IOException {
        return findAll(ActiveCall.class);
    }

    @Override
    public ActiveCall getActiveCall(UUID id) throws IOException {
        requireNonNull(id, "id must no be null");
        return findById(id.toString(), ActiveCall.class);
    }

    @Override
    public void deleteActiveCall(UUID id) throws IOException {
        Request request = new Request.Builder()
                .url(authenticated().addPathSegment(id.toString()).build())
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            val responseBody = mapper.readValue(response.body().string(), GenericResponse.class);

            if (responseBody != null) {
                responseBody.getFailure().ifPresent(message -> {
                    throw new DeleteResponseException(message);
                });
            }

        }
    }

    public static class CreateResponseException extends RuntimeException {
        CreateResponseException(String message) {
            super(message);
        }
    }

    public static class DeleteResponseException extends RuntimeException {
        DeleteResponseException(String message) {
            super(message);
        }
    }

    @Data
    private static class GenericResponse {
        private Optional<String> ok = Optional.empty();
        private Optional<String> failure = Optional.empty();
    }
}
