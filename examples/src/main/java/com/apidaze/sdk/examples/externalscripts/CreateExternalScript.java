package com.apidaze.sdk.examples.externalscripts;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.externalscripts.ExternalScriptsClient;
import com.apidaze.sdk.client.externalscripts.InvalidURLException;
import com.apidaze.sdk.client.externalscripts.URL;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;

import static java.util.Objects.isNull;

@Slf4j
public class CreateExternalScript {

    public static void main(String... args) {

        val apiKey = System.getenv("API_KEY");
        val apiSecret = System.getenv("API_SECRET");

        if (isNull(apiKey) || isNull(apiSecret)) {
            log.error("System environment variables API_KEY and API_SECRET must be set.");
            System.exit(1);
        }

        val scriptName = "Some cool script";
        val scriptUrl = "http://cool.script.com";

        // initiate the client
        val externalScripts = ExternalScriptsClient.create(new Credentials(apiKey, apiSecret));

        try {
            // create an external script
            val createdScript = externalScripts.create(scriptName, URL.fromString(scriptUrl));
            log.info("Created {}", createdScript);
        } catch (IOException e) {
            log.error("An error occurred during communicating with API", e);
        } catch (InvalidURLException e) {
            log.error("newScriptUrl is invalid ", e);
        }
    }
}
