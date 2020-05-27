package com.apidaze.sdk.examples.mediafiles;

import com.apidaze.sdk.client.ApplicationAction;
import com.apidaze.sdk.client.base.Credentials;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;

import static java.util.Objects.isNull;

@Slf4j
public class ListMediaFilesExample {

    public static void main(String... args) {

        val apiKey = System.getenv("API_KEY");
        val apiSecret = System.getenv("API_SECRET");

        if (isNull(apiKey) || isNull(apiSecret)) {
            log.error("System environment variables API_KEY and API_SECRET must be set.");
            System.exit(1);
        }

        // initiate ApplicationAction
        val applicationAction = ApplicationAction.create(new Credentials(apiKey, apiSecret));

        try {
            // get media files
            val result = applicationAction.getMediaFiles();

            // get media files with parameters
//            val result = applicationAction.getMediaFiles(null, 1, null);

            // get media file names
//            val result = applicationAction.getMediaFileNames();

            // get media file names with parameters
//            val result = applicationAction.getMediaFileNames(null, 2, null);

            log.info("Media files result: {}", result);
        } catch (IOException e) {
            log.error("An error occurred during communicating with API", e);
        }
    }
}
