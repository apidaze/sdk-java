package com.apidaze.sdk.examples;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.recordings.RecordingsClient;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import static java.util.Objects.isNull;

@Slf4j
public class RecordingsExample {

    public static void main(String... args) {

        val apiKey = System.getenv("API_KEY");
        val apiSecret = System.getenv("API_SECRET");

        if (isNull(apiKey) || isNull(apiSecret)) {
            log.error("System environment variables API_KEY and API_SECRET must be set.");
            System.exit(1);
        }

        // initiate the client using create method
        val recordings = RecordingsClient.create(new Credentials(apiKey, apiSecret));

        // get recordings list
        val response = recordings.list();

        log.info("Recordings: {}", response);
    }
}
