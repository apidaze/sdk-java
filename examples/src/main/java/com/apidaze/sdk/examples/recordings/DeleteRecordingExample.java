package com.apidaze.sdk.examples.recordings;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.recordings.RecordingsClient;
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

        // initiate the client
        val recordings = RecordingsClient.create(new Credentials(apiKey, apiSecret));

        // the name of the file to be deleted
        val fileName = "example1.wav";

        try {
            recordings.delete(fileName);
            log.info("File {} has been deleted", fileName);
        } catch (IOException e) {
            log.error("An error occurred during communicating with API", e);
        }
    }
}