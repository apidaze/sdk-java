package com.apidaze.sdk.examples.validates;

import com.apidaze.sdk.client.ApplicationAction;
import com.apidaze.sdk.client.base.Credentials;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;

import static java.util.Objects.isNull;

@Slf4j
public class CredentialsValidationExample {

    public static void main(String... args) {

        val apiKey = System.getenv("API_KEY");
        val apiSecret = System.getenv("API_SECRET");

        if (isNull(apiKey) || isNull(apiSecret)) {
            log.error("System environment variables API_KEY and API_SECRET must be set.");
            System.exit(1);
        }

        // initiate the client
        val applicationAction = ApplicationAction.create(new Credentials(apiKey, apiSecret));

        try {
            // validate credentials
            if (applicationAction.validateCredentials()) {
                log.info("Credentials are valid.");
            } else {
                log.error("Credentials are not valid.");
            }
        } catch (IOException e) {
            log.error("An error occurred during communicating with API", e);
        }
    }
}
