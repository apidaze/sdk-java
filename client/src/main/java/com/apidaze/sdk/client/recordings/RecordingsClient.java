package com.apidaze.sdk.client.recordings;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.base.Credentials;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class RecordingsClient extends BaseApiClient implements Recordings {

    @Getter
    private final String basePath = "recordings";
    @Getter
    private final Credentials credentials;
    @Getter
    private final String baseUrl;

    public static RecordingsClient create(Credentials credentials) {
        requireNonNull(credentials, "Credentials must not be null.");
        return new RecordingsClient(credentials, DEFAULT_BASE_URL);
    }

    @Override
    public List<String> list() throws IOException {
        Request request = new Request.Builder()
                .url(authenticatedUrl())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return objectMapper.readValue(response.body().string(), new TypeReference<List<String>>() {});
        }
    }
}
