package com.apidaze.sdk.client.recordings;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.base.Credentials;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.Files.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@AllArgsConstructor(access = PRIVATE)
public class RecordingsClient extends BaseApiClient implements Recordings {

    private static final String TEMP_FILE_PREFIX = "recordings-";

    @Getter(PROTECTED)
    private final String basePath = "recordings";
    @Getter(PROTECTED)
    private final Credentials credentials;
    @Getter(PROTECTED)
    private final String baseUrl;

    public static RecordingsClient create(Credentials credentials) {
        return create(credentials, DEFAULT_BASE_URL);
    }

    static RecordingsClient create(Credentials credentials, String baseUrl) {
        requireNonNull(credentials, "Credentials must not be null.");
        requireNonNull(baseUrl, "baseUrl must not be null.");

        return new RecordingsClient(credentials, baseUrl);
    }

    @Override
    public List<String> list() throws IOException {
        Request request = new Request.Builder()
                .url(authenticatedUrl())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return objectMapper.readValue(response.body().string(), new TypeReference<List<String>>() {
            });
        }
    }

    @Override
    public InputStream download(String sourceFileName) throws IOException {
        Request request = downloadRequest(sourceFileName);
        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return response.body().byteStream();
    }

    @Override
    public void downloadToFileAsync(String sourceFileName, Path targetDir, Callback callback) {
        downloadToFileAsync(sourceFileName, targetDir, false, callback);
    }

    @Override
    public void downloadToFileAsync(String sourceFileName, Path targetDir, boolean replaceExisting, Callback callback) {
        try {
            downloadToFileAsyncInternal(sourceFileName, targetDir, replaceExisting, callback);
        } catch (Throwable e) {
            callback.onFailure(e);
        }
    }

    private void downloadToFileAsyncInternal(String sourceFileName, Path targetDir, boolean replaceExisting, Callback callback) throws IOException {
        Request request = downloadRequest(sourceFileName);
        Path destFile = getOrCreateFilePath(targetDir, sourceFileName, replaceExisting);
        Path tempFile = createTempFile(targetDir, TEMP_FILE_PREFIX, null);

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handleDownloadToFileError(e, callback, tempFile);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try (ResponseBody responseBody = response.body();
                     Sink fileSink = Okio.sink(tempFile);
                     BufferedSink bufferedSink = Okio.buffer(fileSink)) {

                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }

                    bufferedSink.writeAll(responseBody.source());
                    move(tempFile, destFile, REPLACE_EXISTING);
                    callback.onSuccess(destFile.toFile());
                } catch (Throwable e) {
                    handleDownloadToFileError(e, callback, tempFile);
                }
            }
        });
    }

    @Override
    public File downloadToFile(final String sourceFileName, @NotNull final Path targetDir) throws IOException {
        return downloadToFile(sourceFileName, targetDir, false);
    }

    @Override
    public File downloadToFile(final String sourceFileName, final Path targetDir, boolean replaceExisting) throws IOException {
        Request request = downloadRequest(sourceFileName);
        Path destFile = getOrCreateFilePath(targetDir, sourceFileName, replaceExisting);
        Path tempFile = createTempFile(targetDir, TEMP_FILE_PREFIX, null);

        try (Response response = client.newCall(request).execute();
             Sink fileSink = Okio.sink(tempFile);
             BufferedSink bufferedSink = Okio.buffer(fileSink)) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            bufferedSink.writeAll(response.body().source());
            move(tempFile, destFile, REPLACE_EXISTING);
            return destFile.toFile();
        } finally {
            deleteIfExists(tempFile);
        }
    }

    @Override
    public void delete(String fileName) throws IOException {
        requireNonNull(fileName, "fileName must not be null");

        Request request = new Request.Builder()
                .url(authenticated().addPathSegment(fileName).build())
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        }
    }

    private static Path getOrCreateFilePath(Path dir, String fileName, boolean replaceExisting) throws IOException {
        if (exists(dir)) {
            if (isDirectory(dir)) {
                val destFile = dir.resolve(fileName);
                if (!replaceExisting && Files.exists(destFile)) {
                    throw new FileAlreadyExistsException(destFile.toAbsolutePath().toString());
                }
                return destFile;
            } else {
                throw new NotDirectoryException(dir.toString());
            }
        } else {
            return createDirectories(dir).resolve(fileName);
        }
    }

    private static void handleDownloadToFileError(Throwable e, Callback callback, Path tempFile) {
        try {
            deleteIfExists(tempFile);
            callback.onFailure(e);
        } catch (IOException ex) {
            callback.onFailure(ex.initCause(e));
        }
    }

    private Request downloadRequest(String sourceFileName) {
        return new Request.Builder()
                .url(authenticated()
                        .addPathSegment(sourceFileName)
                        .build())
                .build();
    }

}
