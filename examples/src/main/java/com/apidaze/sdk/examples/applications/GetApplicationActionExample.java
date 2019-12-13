package com.apidaze.sdk.examples.applications;

import com.apidaze.sdk.client.ApplicationManager;
import com.apidaze.sdk.client.base.Credentials;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;

import static java.util.Objects.isNull;

@Slf4j
public class GetApplicationActionExample {

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
            // get application by id
            val id = 3023L;
            val appActionById = applicationManager.getApplicationActionById(id);
            if (appActionById.isPresent()) {
                log.info("Retrieved ApplicationAction for application with id = {}", id);
            } else {
                log.error("Application with id = {} not found.", id);
            }

            // get application by api_key
            val subAppApiKey = "n8fetkvn";
            val appActionByApiKey = applicationManager.getApplicationActionByApiKey(subAppApiKey);
            if (appActionByApiKey.isPresent()) {
                log.info("Retrieved ApplicationAction for application with api_key = {}", subAppApiKey);
            } else {
                log.error("Application with api_key = {} not found.", subAppApiKey);
            }

            // get application by name
            val name = "NEW APPLICATION";
            val appActionByName = applicationManager.getApplicationActionByName(name);
            if (appActionByName.isPresent()) {
                log.info("Retrieved ApplicationAction for application with name = {}", name);
            } else {
                log.error("Application with name = {} not found.", name);
            }
        } catch (IOException e) {
            log.error("An error occurred during communicating with API", e);
        }
    }
}
