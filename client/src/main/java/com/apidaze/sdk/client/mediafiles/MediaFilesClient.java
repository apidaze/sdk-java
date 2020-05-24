package com.apidaze.sdk.client.mediafiles;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.base.Credentials;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.nio.file.Files.isRegularFile;
import static java.util.Collections.emptyMap;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class MediaFilesClient extends BaseApiClient<MediaFile> implements MediaFiles {

    static final MediaType MEDIA_TYPE_AUDIO_WAV = MediaType.parse("audio/wav");

    static final String PARAM_DETAILS = "details";
    static final String PARAM_FILTER = "filter";
    static final String PARAM_MAX_ITEMS = "max_items";
    static final String PARAM_LAST_TOKEN = "last_token";
    static final String PARAM_MEDIA_FILE = "mediafile";
    static final String HEADER_LIST_TRUNCATION_TOKEN = "List-Truncation-Token";

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
    public String uploadMediaFile(Path filePath) throws IOException {
        requireNonNull(filePath, "filePath must not be null");
        if (!isRegularFile(filePath))
            throw new InvalidPathException(filePath.toString(), "filePath is not a regular file");

        val fileName = filePath.getFileName().toString();
        return uploadMediaFile(filePath, fileName);
    }

    @Override
    public String uploadMediaFile(Path filePath, String fileName) throws IOException {
        requireNonNull(fileName, "fileName must not be null");
        requireNonNull(filePath, "filePath must not be null");
        if (!isRegularFile(filePath))
            throw new InvalidPathException(filePath.toString(), "filePath is not a regular file");

        val requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(PARAM_MEDIA_FILE, fileName,
                        RequestBody.create(filePath.toFile(), MEDIA_TYPE_AUDIO_WAV))
                .build();

        Request request = new Request.Builder()
                .url(authenticatedUrl())
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Override
    public InputStream downloadMediaFile(String fileName) throws IOException {
        val request = new Request.Builder()
                .url(authenticated()
                        .addPathSegment(fileName)
                        .build())
                .build();

        val response = client.newCall(request).execute();
        return response.body().byteStream();
    }

    @Override
    public MediaFileSummary getMediaFileSummary(String fileName) throws IOException {
        val request = new Request.Builder()
                .url(authenticated()
                        .addPathSegment(fileName)
                        .build())
                .head()
                .build();

        try (Response response = client.newCall(request).execute()) {
            val contentLength = Optional.ofNullable(response.header("Content-Length"))
                    .map(Long::parseLong)
                    .orElse(null);

            return MediaFileSummary.builder()
                    .contentLength(contentLength)
                    .contentType(response.header("Content-type"))
                    .date(response.header("Date"))
                    .build();
        }
    }

    @Override
    public void deleteMediaFile(String fileName) throws IOException {
        requireNonNull(fileName, "fileName must not be null");
        delete(fileName);
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
