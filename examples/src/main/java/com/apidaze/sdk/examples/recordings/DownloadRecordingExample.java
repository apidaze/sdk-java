package com.apidaze.sdk.examples.recordings;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.recordings.RecordingsClient;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

        val sourceFileName = "example1.wav";

        // 1. download directly to a file
        // local directory where the files should be stored
        val targetDir = Paths.get("foo/bar");
        // force overwriting local files
        val overwriteExisting = true;

        val downloadedFile = recordings.downloadToFile(sourceFileName, targetDir, overwriteExisting);
        log.info("File {} has been downloaded directly to file to {}", sourceFileName, downloadedFile);

        // 2. Download as a stream
        // Remember to close the stream, the following example uses try-with-resources which automatically closes the stream
        try (InputStream inputStream = recordings.download(sourceFileName)) {
            val targetFile = new File("foo/bar/fileFromStream.wav");
            Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            log.info("File {} has been downloaded using stream to {}", sourceFileName, targetFile);
        }
    }
}
