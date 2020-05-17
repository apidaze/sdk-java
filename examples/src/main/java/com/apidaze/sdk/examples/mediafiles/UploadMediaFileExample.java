package com.apidaze.sdk.examples.mediafiles;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.mediafiles.MediaFilesClient;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;
import java.nio.file.Paths;

import static java.util.Objects.isNull;

@Slf4j
public class UploadMediaFileExample {

    public static void main(String... args) {

        val apiKey = System.getenv("API_KEY");
        val apiSecret = System.getenv("API_SECRET");

        if (isNull(apiKey) || isNull(apiSecret)) {
            log.error("System environment variables API_KEY and API_SECRET must be set.");
            System.exit(1);
        }

        // initiate MediaFilesClient
        val client = MediaFilesClient.create(new Credentials(apiKey, apiSecret));

        try {
            val filePath = Paths.get("client/src/test/resources/data/mediafile.wav");

            // upload media file
            val response = client.uploadMediaFile(filePath);

            // upload media file with new name
//            val fileName = "mediafile2.wav";
//            val response = client.uploadMediaFile(filePath, fileName);

            log.info("Media file has been successfully uploaded: {}", response);
        } catch (IOException e) {
            log.error("An error occurred during communicating with API", e);
        }
    }
}
