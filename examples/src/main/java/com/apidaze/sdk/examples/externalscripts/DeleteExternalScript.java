package com.apidaze.sdk.examples.externalscripts;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.externalscripts.ExternalScriptsClient;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;

import static java.util.Objects.isNull;

@Slf4j
public class DeleteExternalScript {

    public static void main(String... args) {

        val apiKey = System.getenv("API_KEY");
        val apiSecret = System.getenv("API_SECRET");

        if (isNull(apiKey) || isNull(apiSecret)) {
            log.error("System environment variables API_KEY and API_SECRET must be set.");
            System.exit(1);
        }

        // script id to be deleted
        val id = 1L;

        // initiate the client
        val externalScripts = ExternalScriptsClient.create(new Credentials(apiKey, apiSecret));

        // delete an external script
        try {
            externalScripts.deleteExternalScript(id);
            log.info("ExternalScript with id = {} has been deleted.", id);
        } catch (IOException e) {
            log.error("An error occurred during communicating with API", e);
        }
    }
}
