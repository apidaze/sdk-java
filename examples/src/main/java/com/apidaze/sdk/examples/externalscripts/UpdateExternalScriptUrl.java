package com.apidaze.sdk.examples.externalscripts;

import com.apidaze.sdk.client.credentials.Credentials;
import com.apidaze.sdk.client.externalscripts.ExternalScriptsClient;
import com.apidaze.sdk.client.externalscripts.InvalidURLException;
import com.apidaze.sdk.client.externalscripts.URL;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static java.lang.Long.parseLong;

@Slf4j
public class UpdateExternalScriptUrl {

    public static void main(String... args) {

        if (args.length < 4) {
            System.err.println("You must provide: <apiKey> <apiSecret> <scriptId> <newScriptUrl> in the  argument list!");
            System.exit(1);
        }

        val apiKey = args[0];
        val apiSecret = args[1];
        val scriptId = args[2];
        val newScriptUrl = args[3];

        // initiate the client
        val externalScripts = ExternalScriptsClient.builder().credentials(new Credentials(apiKey, apiSecret)).build();

        // create external script
        try {
            val id = parseLong(scriptId);
            val script = externalScripts.updateUrl(id, URL.fromString(newScriptUrl)).block();
            log.info("Updated {}", script);
        } catch (NumberFormatException e) {
            log.error("scriptId must be a number");
        } catch (WebClientResponseException e) {
            log.error("API returned the response with status code = {} and body = {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (InvalidURLException e) {
            log.error("newScriptUrl is invalid ", e);
        }
    }
}
