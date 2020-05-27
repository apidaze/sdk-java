package com.apidaze.sdk.examples.sipusers;

import com.apidaze.sdk.client.ApplicationAction;
import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.http.HttpResponseException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;

import static java.util.Objects.isNull;

@Slf4j
public class CreateSipUserExample {

    public static void main(String... args) {

        val apiKey = System.getenv("API_KEY");
        val apiSecret = System.getenv("API_SECRET");

        if (isNull(apiKey) || isNull(apiSecret)) {
            log.error("System environment variables API_KEY and API_SECRET must be set.");
            System.exit(1);
        }

        val username = "chuck_norris";
        val name = "Chuck Norris";
        val emailAddress = "chuck_norris@domain.com";
        val internalCallerIdNumber = "1234";
        val externalCallerIdNumber = "1234567890";

        // initiate ApplicationAction
        val applicationAction = ApplicationAction.create(new Credentials(apiKey, apiSecret));

        try {
            // create a new Sip User
            val result = applicationAction.createSipUser(
                    username, name, emailAddress, internalCallerIdNumber, externalCallerIdNumber);
            log.info("Created Sip User: {}", result);
        } catch (HttpResponseException e) {
            log.error(e.toString());
        } catch (IOException e) {
            log.error("An IO error occurred during communicating with API", e);
        }
    }
}
