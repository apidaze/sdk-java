package com.apidaze.sdk.examples;

import com.apidaze.sdk.client.base.Credentials;
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

        val apiKey = args[0];
        val apiSecret = args[1];

        // initiate the client using create method
        val recordings = RecordingsClient.create(new Credentials(apiKey, apiSecret));

        // get recordings list
        val response = recordings.list();

        log.info("Recordings: {}", response);
    }
}
