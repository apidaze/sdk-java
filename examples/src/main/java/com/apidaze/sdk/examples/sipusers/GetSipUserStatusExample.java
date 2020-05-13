package com.apidaze.sdk.examples.sipusers;

import com.apidaze.sdk.client.ApplicationAction;
import com.apidaze.sdk.client.base.Credentials;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;

import static java.util.Objects.isNull;

@Slf4j
public class GetSipUserStatusExample {

    public static void main(String... args) {

        val apiKey = System.getenv("API_KEY");
        val apiSecret = System.getenv("API_SECRET");

        if (isNull(apiKey) || isNull(apiSecret)) {
            log.error("System environment variables API_KEY and API_SECRET must be set.");
            System.exit(1);
        }

        // initiate ApplicationAction
        val applicationAction = ApplicationAction.create(new Credentials(apiKey, apiSecret));

        // id of sip user status to be fetched
        val id = 24789L;

        try {
            // get sip user
            val status = applicationAction.getSipUserStatus(id);

            if (status.isPresent()) {
                log.info("Retrieved {}", status.get());
            } else {
                log.error("Sip user with id = {} not found.", id);
            }
        } catch (IOException e) {
            log.error("An error occurred during communicating with API", e);
        }
    }
}
