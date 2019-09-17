package com.apidaze.sdk.examples;

import com.apidaze.sdk.client.credentials.Credentials;
import com.apidaze.sdk.client.recordings.Recordings;
import com.apidaze.sdk.client.recordings.RecordingsClient;
import lombok.val;

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

        // initiate the client using builder
//        Recordings recordings = RecordingsClient.builder()
//                .baseUrl(baseUrl)
//                .credentials(new Credentials(apiKey, apiSecret))
//                .build();

        // get recordings list
        val response = recordings.list().collectList().block();


        System.out.println(response);
    }
}
