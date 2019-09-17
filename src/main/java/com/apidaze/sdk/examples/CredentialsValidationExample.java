package com.apidaze.sdk.examples;

import com.apidaze.sdk.client.credentials.Credentials;
import com.apidaze.sdk.client.credentials.CredentialsValidator;
import lombok.val;

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

        // validate credentials
        val response = validator.validateCredentials().block();

        System.out.println(response);
    }
}
