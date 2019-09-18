package com.apidaze.sdk.examples;

import com.apidaze.sdk.client.credentials.Credentials;
import com.apidaze.sdk.client.credentials.CredentialsValidator;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class CredentialsValidationExample {

    public static void main(String... args) {

        if (args.length < 3) {
            System.err.println("You must provide: <baseUrl> <apiKey> <apiSecret> in the  argument list!");
            System.exit(1);
        }

        final String baseUrl = args[0];
        final String apiKey = args[1];
        final String apiSecret = args[2];

        // initiate the client
        CredentialsValidator validator = CredentialsValidator.create(baseUrl, new Credentials(apiKey, apiSecret));

        // or initiate the client using builder
//        Recordings recordings = RecordingsClient.builder()
//                .baseUrl(baseUrl)
//                .credentials(new Credentials(apiKey, apiSecret))
//                .build();

        // validate credentials
        val response = validator.validateCredentials().block();

        log.info(response);
    }
}
