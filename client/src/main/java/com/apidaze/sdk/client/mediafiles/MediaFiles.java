package com.apidaze.sdk.client.mediafiles;

import lombok.Builder;
import lombok.Value;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

public interface MediaFiles {

    Result<String> getMediaFileNames() throws IOException;

    Result<String> getMediaFileNames(String filter, Integer maxItems, String lastToken) throws IOException;

    Result<MediaFile> getMediaFiles() throws IOException;

    Result<MediaFile> getMediaFiles(String filter, Integer maxItems, String lastToken) throws IOException;

    String uploadMediaFile(Path filePath) throws IOException;

    String uploadMediaFile(Path filePath, String fileName) throws IOException;

    InputStream downloadMediaFile(String fileName) throws IOException;

    MediaFileSummary getMediaFileSummary(String fileName) throws IOException;

    void deleteMediaFile(String fileName) throws IOException;

    @Value
    class Result<T> {
        String lastToken;
        List<T> mediaFiles;
    }

    @Value
    @Builder
    class MediaFileSummary {
        Long contentLength;
        String contentType;
        String date;
    }
}
