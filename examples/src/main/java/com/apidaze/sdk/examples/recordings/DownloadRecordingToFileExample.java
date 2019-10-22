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

        // local directory where the files should be stored
        val targetDir = Paths.get("foo");

        // download the file with 'replaceExisting' mode enabled
        val downloadedFile = recordings.downloadToFile(sourceFileName, targetDir, true);
        log.info("File {} has been downloaded to file to {}", sourceFileName, downloadedFile);
    }
}
