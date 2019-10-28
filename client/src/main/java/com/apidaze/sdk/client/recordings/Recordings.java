package com.apidaze.sdk.client.recordings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

public interface Recordings {
    List<String> getRecordingsList() throws IOException;

    InputStream downloadRecording(final String sourceFileName) throws IOException;

    void downloadRecordingToFileAsync(final String sourceFileName, final Path target, final DownloadCallback downloadCallback);

    void downloadRecordingToFileAsync(final String sourceFileName, final Path target, boolean replaceExisting, final DownloadCallback downloadCallback);

    File downloadRecordingToFile(final String sourceFileName, final Path target) throws IOException;

    File downloadRecordingToFile(final String sourceFileName, final Path target, boolean replaceExisting) throws IOException;

    void deleteRecording(final String fileName) throws IOException;

    interface DownloadCallback {
        void onSuccess(File file);

        void onFailure(String sourceFileName, Path target, Throwable e);
    }
}
