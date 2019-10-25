package com.apidaze.sdk.examples.recordings;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.recordings.Recordings;
import com.apidaze.sdk.client.recordings.RecordingsClient;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.File;
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

        val recordings = RecordingsClient.create(new Credentials(apiKey, apiSecret));

        // the name of the file to be downloaded
        val sourceFileName = "example1.wav";

        // local directory where the files should be stored
        val targetDir = Paths.get("foo");

        // download the file in async mode
        recordings.downloadToFileAsync(sourceFileName, targetDir, new Recordings.Callback() {
            @Override
            public void onSuccess(File file) {
                log.info("The {} file has been downloaded to {}", sourceFileName, file);
                System.exit(0);
            }

            @Override
            public void onFailure(Throwable e) {
                log.error("An error occurred during downloading the file.", e);
                System.exit(1);
            }
        });
    }
}
