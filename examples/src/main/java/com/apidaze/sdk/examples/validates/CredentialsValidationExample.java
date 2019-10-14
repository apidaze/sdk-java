package com.apidaze.sdk.examples.validates;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.validates.CredentialsValidator;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

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
        val validator = CredentialsValidator.builder()
                .credentials(new Credentials(apiKey, apiSecret))
                .build();

        // validate credentials
        val validated = validator.validateCredentials();

        if (validated) {
            log.info("Credentials are valid.");
        } else {
            log.error("Credentials are not valid.");
        }
    }
}
