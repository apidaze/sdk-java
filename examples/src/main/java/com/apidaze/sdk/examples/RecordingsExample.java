package com.apidaze.sdk.examples;

import com.apidaze.sdk.client.credentials.Credentials;
import com.apidaze.sdk.client.recordings.Recordings;
import com.apidaze.sdk.client.recordings.RecordingsClient;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class RecordingsExample {

    public static void main(String... args) {

        if (args.length < 2) {
            System.err.println("You must provide: <apiKey> <apiSecret> in the  argument list!");
            System.exit(1);
        }

        final String apiKey = args[0];
        final String apiSecret = args[1];

        // initiate the client using create method
        Recordings recordings = RecordingsClient.create(new Credentials(apiKey, apiSecret));

        // get recordings list
        val response = recordings.list().collectList().block();

        log.info("Recordings: {}", response);
    }
}
