package com.apidaze.sdk.client.externalscripts;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.http.HttpClient;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class ExternalScriptsClient extends BaseApiClient implements ExternalScripts {

    public static final int MAX_NAME_LENGTH = 40;
    private static final String URL = "url";
    private static final String NAME = "name";

    @Getter
    private final String basePath = "externalscripts";
    @Getter
    private final Credentials credentials;
    @Getter
    private final String baseUrl;

    private final OkHttpClient client;

    public static ExternalScriptsClient create(@NotNull Credentials credentials) {
        return create(credentials, DEFAULT_BASE_URL);
    }

    static ExternalScriptsClient create(@NotNull Credentials credentials, @NotNull String baseUrl) {
        requireNonNull(credentials, "Credentials must not be null.");
        requireNonNull(baseUrl, "baseUrl must not be null.");

        return new ExternalScriptsClient(credentials, baseUrl, HttpClient.getClientInstance());
    }

    @Override
    public List<ExternalScript> getExternalScripts() throws IOException {
        Request request = new Request.Builder()
                .url(authenticatedUrl())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return objectMapper.readValue(response.body().string(), new TypeReference<List<ExternalScript>>() {});
        }
    }

    @Override
    public ExternalScript createExternalScript(String name, URL url) throws IOException {
        validateName(name);

        RequestBody formBody = new FormBody.Builder()
                .add(NAME, name)
                .add(URL, url.getValue())
                .build();

        Request request = new Request.Builder()
                .url(authenticatedUrl())
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return objectMapper.readValue(response.body().string(), ExternalScript.class);
        }
    }

    @Override
    public ExternalScript getExternalScript(Long id) throws IOException {
        requireNonNull(id, "id must not be null");

        Request request = new Request.Builder()
                .url(authenticated()
                        .addPathSegment(id.toString())
                        .build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return objectMapper.readValue(response.body().string(), ExternalScript.class);
        }
    }

    @Override
    public ExternalScript updateExternalScript(Long id, String name, URL url) throws IOException {
        requireNonNull(id, "id must not be null");
        requireNonNull(url, "url must not be null");
        validateName(name);

        RequestBody formBody = new FormBody.Builder()
                .add(NAME, name)
                .add(URL, url.getValue())
                .build();

        Request request = new Request.Builder()
                .url(authenticated()
                        .addPathSegment(id.toString())
                        .build())
                .put(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return objectMapper.readValue(response.body().string(), ExternalScript.class);
        }
    }

    @Override
    public ExternalScript updateExternalScriptUrl(Long id, URL url) throws IOException {
        requireNonNull(id, "id must not be null");

        RequestBody formBody = new FormBody.Builder()
                .add(URL, url.getValue())
                .build();

        Request request = new Request.Builder()
                .url(authenticated()
                        .addPathSegment(id.toString())
                        .build())
                .put(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return objectMapper.readValue(response.body().string(), ExternalScript.class);
        }
    }


    @Override
    public void deleteExternalScript(Long id) throws IOException {
        requireNonNull(id, "id must not be null");

        Request request = new Request.Builder()
                .url(authenticated().addPathSegment(id.toString()).build())
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        }
    }

    private static void validateName(String name) {
        requireNonNull(name, "name must not be null");
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("name: maximum " + MAX_NAME_LENGTH + " characters long");
        }
    }
}
