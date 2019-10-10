package com.apidaze.sdk.examples.externalscripts;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.externalscripts.ExternalScriptsClient;
import com.apidaze.sdk.client.externalscripts.InvalidURLException;
import com.apidaze.sdk.client.externalscripts.URL;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static java.util.Objects.isNull;

@Slf4j
public class UpdateExternalScriptUrl {

    public static void main(String... args) {

        val apiKey = System.getenv("API_KEY");
        val apiSecret = System.getenv("API_SECRET");

        if (isNull(apiKey) || isNull(apiSecret)) {
            log.error("System environment variables API_KEY and API_SECRET must be set.");
            System.exit(1);
        }

        // id of updated script
        val id = 1L;
        // new url
        val newScriptUrl = "http://new.cool.script.com";

        // initiate the client
        val externalScripts = ExternalScriptsClient.builder().credentials(new Credentials(apiKey, apiSecret)).build();

        // create external script
        try {
            val script = externalScripts.updateUrl(id, URL.fromString(newScriptUrl));
            log.info("Updated {}", script);
        } catch (WebClientResponseException e) {
            log.error("API returned the response with status code = {} and body = {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (InvalidURLException e) {
            log.error("newScriptUrl is invalid ", e);
        }
    }
}
