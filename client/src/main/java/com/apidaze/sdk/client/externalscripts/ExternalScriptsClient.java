package com.apidaze.sdk.client.externalscripts;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.common.URL;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class ExternalScriptsClient extends BaseApiClient<ExternalScript> implements ExternalScripts {

    private static final String PARAM_URL = "url";
    private static final String PARAM_NAME = "name";

    @Getter
    private final String basePath = "externalscripts";
    @Getter
    private final Credentials credentials;
    @Getter
    private final String baseUrl;

    public static ExternalScriptsClient create(@NotNull Credentials credentials) {
        return create(credentials, DEFAULT_BASE_URL);
    }

    static ExternalScriptsClient create(@NotNull Credentials credentials, @NotNull String baseUrl) {
        requireNonNull(credentials, "Credentials must not be null.");
        requireNonNull(baseUrl, "baseUrl must not be null.");

        return new ExternalScriptsClient(credentials, baseUrl);
    }

    @Override
    public List<ExternalScript> getExternalScripts() throws IOException {
        return findAll(ExternalScript.class);
    }

    @Override
    public Optional<ExternalScript> getExternalScript(Long id) throws IOException {
        requireNonNull(id, "id must not be null");
        return findById(id.toString(), ExternalScript.class);
    }

    @Override
    public ExternalScript updateExternalScript(Long id, String name, URL url) throws IOException {
        requireNonNull(id, "id must not be null");
        requireNonNull(url, "url must not be null");
        validateName(name);

        val params = ImmutableMap.of(PARAM_NAME, name, PARAM_URL, url.getValue());
        return update(id.toString(), params, ExternalScript.class);
    }

    @Override
    public ExternalScript updateExternalScriptUrl(Long id, URL url) throws IOException {
        requireNonNull(id, "id must not be null");
        requireNonNull(url, "url must not be null");

        return update(id.toString(), ImmutableMap.of(PARAM_URL, url.getValue()), ExternalScript.class);
    }

    private static void validateName(String name) {
        requireNonNull(name, "name must not be null");
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("name: maximum " + MAX_NAME_LENGTH + " characters long");
        }
    }
}
