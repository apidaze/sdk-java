package com.apidaze.sdk.examples.externalscripts;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.externalscripts.ExternalScriptsClient;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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
        val externalScripts = ExternalScriptsClient.builder().credentials(new Credentials(apiKey, apiSecret)).build();

        // delete an external script
        try {
            externalScripts.delete(id);
            log.info("ExternalScript with id = {} has been deleted.", id);
        } catch (WebClientResponseException e) {
            log.error("API returned the response with status code = {} and body = {}", e.getStatusCode(), e.getResponseBodyAsString());
        }
    }
}
