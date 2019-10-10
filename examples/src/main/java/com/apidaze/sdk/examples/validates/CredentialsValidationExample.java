package com.apidaze.sdk.examples.validates;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.validates.CredentialsValidator;
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
        val validator = CredentialsValidator.create(new Credentials(apiKey, apiSecret));

        try {
            // validate credentials
            if (validator.validateCredentials()) {
                log.info("Credentials are valid.");
            } else {
                log.error("Credentials are not valid.");
            }
        } catch (IOException e) {
            log.error("An error occurred during communicating with API", e);
        }
    }
}
