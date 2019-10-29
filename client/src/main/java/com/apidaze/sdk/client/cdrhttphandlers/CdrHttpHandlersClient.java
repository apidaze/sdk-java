package com.apidaze.sdk.client.cdrhttphandlers;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.http.HttpClient;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import static java.util.Objects.requireNonNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CdrHttpHandlersClient extends BaseApiClient implements CdrHttpHandlers {

    @Getter
    private final String basePath = "cdrhttphandlers";
    @Getter
    private final Credentials credentials;
    @Getter
    private final String baseUrl;

    private final OkHttpClient client;

    public static CdrHttpHandlersClient create(@NotNull Credentials credentials) {
        return create(credentials, DEFAULT_BASE_URL);
    }

    static CdrHttpHandlersClient create(@NotNull Credentials credentials, @NotNull String baseUrl) {
        requireNonNull(credentials, "Credentials must not be null.");
        requireNonNull(baseUrl, "baseUrl must not be null.");

        return new CdrHttpHandlersClient(credentials, baseUrl, HttpClient.getClientInstance());
    }


    @Override
    public List<CdrHttpHandler> getCdrHttpHandlers() throws IOException {
        Request request = new Request.Builder()
                .url(authenticatedUrl())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return objectMapper.readValue(response.body().string(), new TypeReference<List<CdrHttpHandler>>() {});
        }
    }
}
