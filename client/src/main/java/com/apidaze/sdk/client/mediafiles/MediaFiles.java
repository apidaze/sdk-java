package com.apidaze.sdk.client.mediafiles;

import lombok.Value;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface MediaFiles {

    Result<String> getMediaFileNames() throws IOException;

    Result<String> getMediaFileNames(String filter, Integer maxItems, String lastToken) throws IOException;

    Result<MediaFile> getMediaFiles() throws IOException;

    Result<MediaFile> getMediaFiles(String filter, Integer maxItems, String lastToken) throws IOException;

    String uploadMediaFile(Path filePath) throws IOException;

    String uploadMediaFile(Path filePath, String fileName) throws IOException;

    @Value
    class Result<T> {
        String lastToken;
        List<T> mediaFiles;
    }
}
