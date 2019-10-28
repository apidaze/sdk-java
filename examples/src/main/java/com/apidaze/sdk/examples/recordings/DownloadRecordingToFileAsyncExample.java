package com.apidaze.sdk.examples.recordings;

import com.apidaze.sdk.client.ApplicationAction;
import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.recordings.Recordings;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Objects.isNull;

@Slf4j
public class DownloadRecordingToFileAsyncExample {

    public static void main(String... args) {

        val apiKey = System.getenv("API_KEY");
        val apiSecret = System.getenv("API_SECRET");

        if (isNull(apiKey) || isNull(apiSecret)) {
            log.error("System environment variables API_KEY and API_SECRET must be set.");
            System.exit(1);
        }

        // initiate ApplicationAction
        val applicationAction = ApplicationAction.create(new Credentials(apiKey, apiSecret));

        // the name of the file to be downloaded
        val sourceFileName = "example1.wav";

        // local directory where the files should be stored
        val targetDir = Paths.get("foo");

        val callback = new Recordings.DownloadCallback() {
            @Override
            public void onSuccess(File file) {
                log.info("The {} file has been downloaded to {}", sourceFileName, file);
            }

            @Override
            public void onFailure(String sourceFileName, Path target, Throwable e) {
                log.error("An error occurred during downloading the file {} to target path {}.", sourceFileName, target, e);
            }
        };

        // download two file in async mode
        val targetFile1 = targetDir.resolve("file1.wav");
        val targetFile2 = targetDir.resolve("file2.wav");

        log.info("Starting downloading the file  {} to target path {}", sourceFileName, targetFile1);
        applicationAction.downloadRecordingToFileAsync(sourceFileName, targetFile1, callback);

        log.info("Starting downloading the file {} to target path {}", sourceFileName, targetFile2);
        applicationAction.downloadRecordingToFileAsync(sourceFileName, targetFile2, callback);
    }
}
