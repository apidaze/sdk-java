package com.apidaze.sdk.client.recordings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface Recordings {
    List<String> list() throws IOException;
    File download(final String sourceFileName, final Path targetDir) throws IOException;
    File download(final String sourceFileName, final Path targetDir, boolean overwrite) throws IOException;
}
