package com.apidaze.sdk.client.recordings;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.base.Credentials;
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
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.Files.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@AllArgsConstructor(access = PRIVATE)
public class RecordingsClient extends BaseApiClient<String> implements Recordings {

    private static final String TEMP_FILE_PREFIX = "apidaze-sdk-recordings-";

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
    public List<String> getRecordingsList() throws IOException {
        return findAll(String.class);
    }

    @Override
    public void deleteRecording(String fileName) throws IOException {
        requireNonNull(fileName, "fileName must not be null");
        delete(fileName);
    }

    @Override
    public InputStream downloadRecording(String sourceFileName) throws IOException {
        val request = downloadRequest(sourceFileName);
        val response = client.newCall(request).execute();
        return response.body().byteStream();
    }

    @Override
    public void downloadRecordingToFileAsync(String sourceFileName, Path target, DownloadCallback callback) {
        downloadRecordingToFileAsync(sourceFileName, target, false, callback);
    }

    @Override
    public void downloadRecordingToFileAsync(String sourceFileName, Path target, boolean replaceExisting, DownloadCallback callback) {
        try {
            downloadToFileAsyncInternal(sourceFileName, target, replaceExisting, callback);
        } catch (Throwable e) {
            callback.onFailure(sourceFileName, target, e);
        }
    }

    private void downloadToFileAsyncInternal(String sourceFileName, Path target, boolean replaceExisting, DownloadCallback callback) throws IOException {
        Request request = downloadRequest(sourceFileName);
        Path destFile = getOrCreateFilePath(target, sourceFileName, replaceExisting);
        Path tempFile = createTempFile(destFile.toAbsolutePath().getParent(), TEMP_FILE_PREFIX, null);

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handleDownloadToFileError(sourceFileName, target, e, callback, tempFile);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try (ResponseBody responseBody = response.body();
                     Sink fileSink = Okio.sink(tempFile);
                     BufferedSink bufferedSink = Okio.buffer(fileSink)
                ) {
                    bufferedSink.writeAll(responseBody.source());
                    move(tempFile, destFile, REPLACE_EXISTING);
                    callback.onSuccess(destFile.toFile());
                } catch (Throwable e) {
                    handleDownloadToFileError(sourceFileName, target, e, callback, tempFile);
                }
            }
        });
    }

    @Override
    public File downloadRecordingToFile(final String sourceFileName, @NotNull final Path target) throws IOException {
        return downloadRecordingToFile(sourceFileName, target, false);
    }

    @Override
    public File downloadRecordingToFile(final String sourceFileName, final Path target, boolean replaceExisting) throws IOException {
        Request request = downloadRequest(sourceFileName);
        Path destFile = getOrCreateFilePath(target, sourceFileName, replaceExisting);
        Path tempFile = createTempFile(destFile.toAbsolutePath().getParent(), TEMP_FILE_PREFIX, null);

        try (Response response = client.newCall(request).execute();
             Sink fileSink = Okio.sink(tempFile);
             BufferedSink bufferedSink = Okio.buffer(fileSink)
        ) {
            bufferedSink.writeAll(response.body().source());
            move(tempFile, destFile, REPLACE_EXISTING);
            return destFile.toFile();
        } finally {
            deleteIfExists(tempFile);
        }
    }

    private static Path getOrCreateFilePath(Path target, String sourceFile, boolean replaceExisting) throws IOException {
        if (isRegularFile(target)) {
            return getPathForRegularFile(target, replaceExisting);
        } else if (isDirectory(target)) {
            return getPathForDirectory(target, sourceFile, replaceExisting);
        } else if (target.getFileName().toString().contains(".")) {
            createDirectories(target.toAbsolutePath().getParent());
            return target;
        } else {
            return createDirectories(target).resolve(sourceFile);
        }
    }

    private static Path getPathForRegularFile(Path target, boolean replaceExisting) throws FileAlreadyExistsException {
        if (replaceExisting) {
            return target;
        } else {
            throw new FileAlreadyExistsException(target.toAbsolutePath().toString());
        }
    }

    private static Path getPathForDirectory(Path target, String sourceFile, boolean replaceExisting) throws FileAlreadyExistsException {
        val desFile = target.resolve(sourceFile);
        if (Files.exists(desFile)) {
            if (replaceExisting) {
                return desFile;
            } else {
                throw new FileAlreadyExistsException(desFile.toAbsolutePath().toString());
            }
        } else {
            return desFile;
        }
    }

    private static void handleDownloadToFileError(String sourceFileName, Path target, Throwable e, DownloadCallback downloadCallback, Path tempFile) {
        try {
            deleteIfExists(tempFile);
            downloadCallback.onFailure(sourceFileName, target, e);
        } catch (IOException ex) {
            downloadCallback.onFailure(sourceFileName, target, ex.initCause(e));
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
