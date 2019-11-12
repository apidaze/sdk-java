package com.apidaze.sdk.client.recordings;

import com.apidaze.sdk.client.http.HttpResponseException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.util.List;

public interface Recordings {

    /**
     * Returns the list of recordings for your domain.
     *
     * @return list of file names
     * @throws IOException
     * @throws HttpResponseException
     */
    List<String> getRecordingsList() throws IOException;

    /**
     * Returns recording file as {@link InputStream}.
     * To avoid leaking resources callers should close {@link InputStream}.
     *
     * @param sourceFileName the name of the file to be downloaded
     * @return {@link InputStream}
     * @throws IOException
     * @throws HttpResponseException
     */
    InputStream downloadRecording(final String sourceFileName) throws IOException;

    /**
     * Downloads the file in asynchronous mode. The file is downloaded to local disk.
     * Once completed an appropriate method from {@link DownloadCallback} is called with either the downloaded file
     * in case of success or with an exception in case of failure.
     * <p>This method will invoke {@code downloadCallback.onFailure} with {@code FileAlreadyExistsException} if:
     * <ul>
     *  <li>the target path is an existing file</li>
     *  <li>the target path is an existing directory and the file with name from parameter {@code sourceFileName}
     *  exists in this directory</li>
     * </ul>
     *
     * @param sourceFileName   the name of the file to be downloaded
     * @param target           the path where the downloaded file is stored. If the path is a directory the file will
     *                         keep the original name. If you want to change the downloaded file name provide the path which
     *                         represents a file.
     * @param downloadCallback see {@link DownloadCallback}
     */
    void downloadRecordingToFileAsync(final String sourceFileName, final Path target, final DownloadCallback downloadCallback);

    /**
     * Downloads the file in asynchronous mode. The file is downloaded to local disk.
     * Once completed an appropriate method from {@link DownloadCallback} is called with either the downloaded file
     * in case of success or with an exception in case of failure.
     * <p>This method will invoke {@code downloadCallback.onFailure} with {@code FileAlreadyExistsException} if
     * {@code replaceExisting} is {@code false} and:
     * <ul>
     *  <li>the target path is an existing file</li>
     *  <li>the target path is an existing directory and the file with name from parameter {@code sourceFileName}
     *  exists in this directory</li>
     * </ul>
     *
     * @param sourceFileName   the name of the file to be downloaded
     * @param target           the path where the downloaded file is stored. If the path is a directory the file will
     *                         keep the original name. If you want to change the downloaded file name provide the path which
     *                         represents a file.
     * @param replaceExisting  set to {@code true} if you want to replace an existing file in local disk
     * @param downloadCallback see {@link DownloadCallback}
     */
    void downloadRecordingToFileAsync(final String sourceFileName, final Path target, boolean replaceExisting, final DownloadCallback downloadCallback);

    /**
     * Download the file to disk.
     *
     * @param sourceFileName the name of the file to be downloaded
     * @param target         the path where the downloaded file is stored. If the path is a directory the file will
     *                       keep the original name. If you want to change the downloaded file name provide the path which
     *                       represents a file.
     * @return
     * @throws IOException
     * @throws HttpResponseException
     * @throws FileAlreadyExistsException <ul>
     *                                    <li>if the target path is an existing file</li>
     *                                    <li>if the target path is an existing directory and the file with name from parameter {@code sourceFileName}
     *                                    exists in this directory</li>
     *                                    </ul>
     */
    File downloadRecordingToFile(final String sourceFileName, final Path target) throws IOException;

    /**
     * Downloads the file to disk. This is blocking operation.
     *
     * @param sourceFileName  the name of the file to be downloaded
     * @param replaceExisting set to {@code true} if you want to replace an existing file in local disk
     * @param target          the path where the downloaded file is stored. If the path is a directory the file will
     *                        keep the original name. If you want to change the downloaded file name provide the path which
     *                        represents a file.
     * @return
     * @throws IOException
     * @throws HttpResponseException
     * @throws FileAlreadyExistsException <p>if {@code replaceExisting} is {@code false} and:
     *                                    <ul>
     *                                           <li>the target path is an existing file</li>
     *                                           <li>the target path is an existing directory and the file with name from parameter {@code sourceFileName}
     *                                    </ul>
     */
    File downloadRecordingToFile(final String sourceFileName, final Path target, boolean replaceExisting) throws IOException;

    /**
     * Deletes recording with provided file name.
     *
     * @param fileName the name of the file to be deleted
     * @throws IOException
     * @throws HttpResponseException
     */
    void deleteRecording(final String fileName) throws IOException;

    interface DownloadCallback {
        /**
         * Called when the file was successfully downloaded.
         *
         * @param file a downloaded file
         */
        void onSuccess(File file);

        /**
         * Called when an error occurred during downloading a file
         *
         * @param sourceFileName the name of the file which was tried to be downloaded
         * @param target         the path where the downloaded file was tried to be stored
         * @param e              an exception caused the error
         */
        void onFailure(String sourceFileName, Path target, Throwable e);
    }
}
