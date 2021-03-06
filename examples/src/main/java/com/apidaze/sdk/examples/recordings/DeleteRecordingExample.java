package com.apidaze.sdk.examples.recordings;

import com.apidaze.sdk.client.ApplicationAction;
import com.apidaze.sdk.client.base.Credentials;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;

import static java.util.Objects.isNull;

@Slf4j
public class DeleteRecordingExample {

    public static void main(String... args) {

        val apiKey = System.getenv("API_KEY");
        val apiSecret = System.getenv("API_SECRET");

        if (isNull(apiKey) || isNull(apiSecret)) {
            log.error("System environment variables API_KEY and API_SECRET must be set.");
            System.exit(1);
        }

        // initiate ApplicationAction
        val applicationAction = ApplicationAction.create(new Credentials(apiKey, apiSecret));

        // the name of the file to be deleted
        val fileName = "example1.wav";

        try {
            applicationAction.deleteRecording(fileName);
            log.info("File {} has been deleted", fileName);
        } catch (IOException e) {
            log.error("An error occurred during communicating with API", e);
        }
    }
}
