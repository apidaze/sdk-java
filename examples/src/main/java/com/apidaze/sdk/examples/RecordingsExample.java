package com.apidaze.sdk.examples;

import com.apidaze.sdk.client.credentials.Credentials;
import com.apidaze.sdk.client.recordings.Recordings;
import com.apidaze.sdk.client.recordings.RecordingsClient;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class RecordingsExample {

    public static void main(String... args) {

        if (args.length < 3) {
            System.err.println("You must provide: <baseUrl> <apiKey> <apiSecret> in the  argument list!");
            System.exit(1);
        }

        final String baseUrl = args[0];
        final String apiKey = args[1];
        final String apiSecret = args[2];

        // initiate the client using create method
        Recordings recordings = RecordingsClient.create(baseUrl, new Credentials(apiKey, apiSecret));

        // get recordings list
        val response = recordings.list().collectList().block();

        log.info("Recordings: {}", response);
    }
}
