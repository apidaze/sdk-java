package com.apidaze.sdk.client.mediafiles;

import com.apidaze.sdk.client.http.HttpResponseException;
import lombok.Builder;
import lombok.Value;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

/**
 * The interface manages media files
 */
public interface MediaFiles {

    /**
     * Returns the list of all media file names for the application.
     *
     * @return the list of media file names
     * @throws IOException
     * @throws HttpResponseException
     */
    Result<String> getMediaFileNames() throws IOException;

    /**
     * Returns the list of media file names for the application with an option to filter and limit the result size.
     *
     * @param filter    Response data will only include files matching exact string to filter.
     * @param maxItems  Max number of file listings to return. If this limit is reached for a response,
     *                  a {@code Result.lastToken} will contain the token to use in a subsequent call
     *                  with the lastToken argument of this function.
     * @param lastToken This should only be used if you are continuing a partial request.
     *                  Supply the value from a previous request's lastToken response
     *                  to continue with partitioned data.
     * @return the list of media file names with lastToken if the limit is reached
     * @throws IOException
     * @throws HttpResponseException
     */
    Result<String> getMediaFileNames(String filter, Integer maxItems, String lastToken) throws IOException;

    /**
     * Returns the list of all media files for the application.
     *
     * @return the list of {@link MediaFile} instances with lastToken if the limit is reached
     * @throws IOException
     * @throws HttpResponseException
     */
    Result<MediaFile> getMediaFiles() throws IOException;

    /**
     * Returns the list of media files for the application with an option to filter and limit the result size.
     *
     * @param filter    Response data will only include files matching exact string to filter.
     * @param maxItems  Max number of file listings to return. If this limit is reached for a response,
     *                  a {@code Result.lastToken} will contain the token to use in a subsequent call
     *                  with the lastToken argument of this function.
     * @param lastToken This should only be used if you are continuing a partial request.
     *                  Supply the value from a previous request's lastToken response
     *                  to continue with partitioned data.
     * @return the list of {@link MediaFile} instances with lastToken if the limit is reached
     * @throws IOException
     * @throws HttpResponseException
     */
    Result<MediaFile> getMediaFiles(String filter, Integer maxItems, String lastToken) throws IOException;

    /**
     * Upload a media file for an application.
     * Media files can be used in playback tags by simply referencing the uploaded file name.
     * WAV Files will be converted to 8k, 16bit, 1channel audio. For best quality and fastest processing,
     * supply an audio file with these exact specs.
     *
     * @param filePath file path to upload
     * @return a message containing the response form the server
     * @throws IOException
     * @throws HttpResponseException
     */
    String uploadMediaFile(Path filePath) throws IOException;

    /**
     * Upload a media file for an application.
     * Media files can be used in playback tags by simply referencing the uploaded file name.
     * WAV Files will be converted to 8k, 16bit, 1channel audio. For best quality and fastest processing,
     * supply an audio file with these exact specs.
     *
     * @param filePath the file path to upload
     * @param fileName a new name of the uploaded file
     * @return a message containing the response form the server
     * @throws IOException
     * @throws HttpResponseException
     */
    String uploadMediaFile(Path filePath, String fileName) throws IOException;

    /**
     * Downloads a media file. To avoid leaking resources callers should close {@link InputStream}.
     *
     * @param fileName the file name to be downloaded
     * @return {@link InputStream} containing the content of the file
     * @throws IOException
     * @throws HttpResponseException
     */
    InputStream downloadMediaFile(String fileName) throws IOException;

    /**
     * Shows a media file summary.
     *
     * @param fileName The filename with any custom path.
     *                 Examples: test_playback_file.wav, clients/bob/test_playback_file.wav
     * @return {@link MediaFileSummary} instance
     * @throws IOException
     * @throws HttpResponseException
     */
    MediaFileSummary getMediaFileSummary(String fileName) throws IOException;

    /**
     * Deletes a media file from an application.
     *
     * @param fileName The filename with any custom path.
     *                 Examples: test_playback_file.wav, clients/bob/test_playback_file.wav
     * @throws IOException
     * @throws HttpResponseException
     */
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
