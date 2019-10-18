package com.apidaze.sdk.examples.recordings;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.recordings.RecordingsClient;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;
import java.nio.file.Paths;

import static java.util.Objects.isNull;

@Slf4j
public class DownloadRecordingExample {

    public static void main(String... args) throws IOException {

        val apiKey = System.getenv("API_KEY");
        val apiSecret = System.getenv("API_SECRET");

        if (isNull(apiKey) || isNull(apiSecret)) {
            log.error("System environment variables API_KEY and API_SECRET must be set.");
            System.exit(1);
        }

        // initiate the client
        val recordings = RecordingsClient.create(new Credentials(apiKey, apiSecret));

        val fileName = "example1.wav";
        // local directory where the files should be stored
        val targetDir = Paths.get("foo/bar");
        // force overwriting local files
        val overwrite = true;

        // download a file
        val downloadedFile = recordings.download(fileName, targetDir, overwrite);
        log.info("File {} has been downloaded to {}", fileName, downloadedFile.getAbsolutePath());
    }
}
