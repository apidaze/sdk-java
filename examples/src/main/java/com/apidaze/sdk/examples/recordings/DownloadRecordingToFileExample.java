package com.apidaze.sdk.examples.recordings;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.recordings.RecordingsClient;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;
import java.nio.file.Paths;

import static java.util.Objects.isNull;

@Slf4j
public class DownloadRecordingToFileExample {

    public static void main(String... args) throws IOException {

        val apiKey = System.getenv("API_KEY");
        val apiSecret = System.getenv("API_SECRET");

        if (isNull(apiKey) || isNull(apiSecret)) {
            log.error("System environment variables API_KEY and API_SECRET must be set.");
            System.exit(1);
        }

        val recordings = RecordingsClient.create(new Credentials(apiKey, apiSecret));

        // the name of the file to be downloaded
        val sourceFileName = "example1.wav";


        // 1. download a file to local directory without changing the name of the file
        val targetDir = Paths.get("foo");

        val downloadedFile1 = recordings.downloadRecordingToFile(sourceFileName, targetDir);
        log.info("The {} file has been downloaded to {}", sourceFileName, downloadedFile1);

        // 2. download a file to local directory and change the name of target file
        val targetFile = Paths.get("foo/my-cool-recoding.wav");

        val downloadedFile2 = recordings.downloadRecordingToFile(sourceFileName, targetFile);
        log.info("The {} file has been downloaded to {}", sourceFileName, downloadedFile2);
    }
}
