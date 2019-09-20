package com.apidaze.sdk.examples;

import com.apidaze.sdk.client.credentials.Credentials;
import com.apidaze.sdk.client.credentials.CredentialsValidator;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class CredentialsValidationExample {

    public static void main(String... args) {

        if (args.length < 2) {
            System.err.println("You must provide: <apiKey> <apiSecret> in the  argument list!");
            System.exit(1);
        }

        val apiKey = args[0];
        val apiSecret = args[1];

        // initiate the client
        val validator = CredentialsValidator.create(new Credentials(apiKey, apiSecret));

        // or initiate the client using builder
//        val validator = CredentialsValidator.builder()
//                .credentials(new Credentials(apiKey, apiSecret))
//                .build();

        // validate credentials
        val response = validator.validateCredentials().block();

        log.info(response);
    }
}
