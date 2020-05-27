package com.apidaze.sdk.examples.mediafiles;

import com.apidaze.sdk.client.ApplicationAction;
import com.apidaze.sdk.client.base.Credentials;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;

import static java.util.Objects.isNull;

@Slf4j
public class ShowMediaFileSummaryExample {

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
            val fileName = "mediafile.wav";
            // get media file summary
            val response = applicationAction.getMediaFileSummary(fileName);
            log.info("Media file summary: {}", response);
        } catch (IOException e) {
            log.error("An error occurred during communicating with API", e);
        }
    }
}
