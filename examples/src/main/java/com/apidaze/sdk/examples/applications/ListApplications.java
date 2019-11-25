package com.apidaze.sdk.examples.applications;

import com.apidaze.sdk.client.ApplicationManager;
import com.apidaze.sdk.client.base.Credentials;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;

import static java.util.Objects.isNull;

@Slf4j
public class ListApplications {

    public static void main(String... args) {

        val apiKey = System.getenv("API_KEY");
        val apiSecret = System.getenv("API_SECRET");

        if (isNull(apiKey) || isNull(apiSecret)) {
            log.error("System environment variables API_KEY and API_SECRET must be set.");
            System.exit(1);
        }

        // initiate ApplicationAction
        val applicationManager = ApplicationManager.create(new Credentials(apiKey, apiSecret));

        try {
            // get applications
            val applications = applicationManager.getApplications();
            log.info("Applications: {}", applications);
        } catch (IOException e) {
            log.error("An error occurred during communicating with API", e);
        }
    }
}
