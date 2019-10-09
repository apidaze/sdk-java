package com.apidaze.sdk.examples.externalscripts;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.externalscripts.ExternalScriptsClient;
import com.apidaze.sdk.client.externalscripts.InvalidURLException;
import com.apidaze.sdk.client.externalscripts.URL;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
public class CreateExternalScript {

    public static void main(String... args) {

        if (args.length < 4) {
            System.err.println("You must provide: <apiKey> <apiSecret> <scriptName> <scriptUrl> in the  argument list!");
            System.exit(1);
        }

        val apiKey = args[0];
        val apiSecret = args[1];
        val scriptName = args[2];
        val scriptUrl = args[3];

        // initiate the client
        val externalScripts = ExternalScriptsClient.builder().credentials(new Credentials(apiKey, apiSecret)).build();

        try {
            // create an external script
            val createdScript = externalScripts.create(scriptName, URL.fromString(scriptUrl));
            log.info("Created {}", createdScript);
        } catch (WebClientResponseException e) {
            log.error("API returned the response with status code = {} and body = {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (InvalidURLException e) {
            log.error("newScriptUrl is invalid ", e);
        }
    }
}
