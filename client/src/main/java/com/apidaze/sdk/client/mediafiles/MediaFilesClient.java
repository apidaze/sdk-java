package com.apidaze.sdk.client.mediafiles;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.base.Credentials;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class MediaFilesClient extends BaseApiClient<MediaFile> implements MediaFiles {

    private static final String PARAM_DETAILS = "details";
    private static final String PARAM_FILTER = "filter";
    private static final String PARAM_MAX_ITEMS = "max_items";
    private static final String PARAM_LAST_TOKEN = "last_token";
    private static final String HEADER_LIST_TRUNCATION_TOKEN = "List-Truncation-Token";

    @Getter
    private final String basePath = "mediafiles";
    @Getter
    private final Credentials credentials;
    @Getter
    private final String baseUrl;

    public static MediaFilesClient create(@NotNull Credentials credentials) {
        return create(credentials, DEFAULT_BASE_URL);
    }

    static MediaFilesClient create(@NotNull Credentials credentials, @NotNull String baseUrl) {
        requireNonNull(credentials, "Credentials must not be null.");
        requireNonNull(baseUrl, "baseUrl must not be null.");

        return new MediaFilesClient(credentials, baseUrl);
    }

    @Override
    public Result<MediaFile> getMediaFiles() throws IOException {
        val parameters = ImmutableMap.of(PARAM_DETAILS, Boolean.TRUE.toString());
        return getResult(parameters, MediaFile.class);
    }

    @Override
    public Result<MediaFile> getMediaFiles(String filter, Integer maxItems, String lastToken) throws IOException {
        if (nonNull(maxItems) && maxItems < 1) {
            throw new IllegalArgumentException("maxItems must be greater than 0");
        }

        val parameters = createMapBuilder(filter, maxItems, lastToken)
                .put(PARAM_DETAILS, Boolean.TRUE.toString())
                .build();

        return getResult(parameters, MediaFile.class);
    }

    @Override
    public Result<String> getMediaFileNames() throws IOException {
        return getResult(emptyMap(), String.class);
    }

    @Override
    public Result<String> getMediaFileNames(String filter, Integer maxItems, String lastToken) throws IOException {
        if (nonNull(maxItems) && maxItems < 1) {
            throw new IllegalArgumentException("maxItems must be greater than 0");
        }

        val parameters = createMapBuilder(filter, maxItems, lastToken).build();

        return getResult(parameters, String.class);
    }

    private ImmutableMap.Builder<String, String> createMapBuilder(String filter, Integer maxItems, String lastToken) {
        val parameters = ImmutableMap.<String, String>builder();

        if (nonNull(filter)) {
            parameters.put(PARAM_FILTER, filter);
        }
        if (nonNull(maxItems)) {
            parameters.put(PARAM_MAX_ITEMS, maxItems.toString());
        }
        if (nonNull(lastToken)) {
            parameters.put(PARAM_LAST_TOKEN, lastToken);
        }

        return parameters;
    }

    private <T> Result<T> getResult(Map<String, String> parameters, Class<T> clazz) throws IOException {
        Request request = new Request.Builder()
                .url(authenticated(parameters).build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            List<T> entities = mapper.readValue(
                    response.body().string(),
                    mapper.getTypeFactory().constructCollectionType(List.class, clazz));

            val lastToken = response.header(HEADER_LIST_TRUNCATION_TOKEN);
            return new Result<>(lastToken, entities);
        }
    }
}
