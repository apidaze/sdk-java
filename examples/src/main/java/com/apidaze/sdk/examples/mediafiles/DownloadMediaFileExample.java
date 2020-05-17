package com.apidaze.sdk.examples.mediafiles;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.mediafiles.MediaFilesClient;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static java.util.Objects.isNull;

@Slf4j
public class DownloadMediaFileExample {

    public static void main(String... args) throws IOException {

        val apiKey = System.getenv("API_KEY");
        val apiSecret = System.getenv("API_SECRET");

        if (isNull(apiKey) || isNull(apiSecret)) {
            log.error("System environment variables API_KEY and API_SECRET must be set.");
            System.exit(1);
        }

        // initiate MediaFilesClient
        val client = MediaFilesClient.create(new Credentials(apiKey, apiSecret));

        // the name of the file to be downloaded
        val sourceFileName = "mediafile.wav";

        // the path of target file to which the stream will copied to
        val targetFilePath = Paths.get("data/mediafile.wav");

        // Remember to close the stream, the following example uses try-with-resources which automatically closes the stream
        try (InputStream inputStream = client.downloadMediaFile(sourceFileName)) {
            Files.copy(inputStream, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
            log.info("The {} file has been downloaded to {}", sourceFileName, targetFilePath);
        }
    }
}
