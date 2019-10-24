package com.apidaze.sdk.client.recordings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

public interface Recordings {
    List<String> list() throws IOException;

    InputStream download(final String sourceFileName) throws IOException;

    void downloadToFileAsync(final String sourceFileName, final Path targetDir, final Callback callback);

    void downloadToFileAsync(final String sourceFileName, final Path targetDir, boolean replaceExisting, final Callback callback);

    File downloadToFile(final String sourceFileName, final Path targetDir) throws IOException;

    File downloadToFile(final String sourceFileName, final Path targetDir, boolean replaceExisting) throws IOException;

    void delete(final String fileName) throws IOException;

    interface Callback {
        void onSuccess(File file);

        void onFailure(Throwable e);
    }
}
