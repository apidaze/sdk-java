package com.apidaze.sdk.client.recordings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

public interface Recordings {
    List<String> list() throws IOException;

    InputStream download(final String sourceFileName) throws IOException;

    File downloadToFile(final String sourceFileName, final Path targetDir) throws IOException;

    File downloadToFile(final String sourceFileName, final Path targetDir, boolean replaceExisting) throws IOException;

    void delete(final String fileName) throws IOException;
}
