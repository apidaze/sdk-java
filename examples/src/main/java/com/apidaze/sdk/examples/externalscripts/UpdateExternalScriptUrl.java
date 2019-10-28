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
        val externalScripts = ExternalScriptsClient.create(new Credentials(apiKey, apiSecret));

        // create external script
        try {
            val script = externalScripts.updateExternalScriptUrl(id, URL.fromString(newScriptUrl));
            log.info("Updated {}", script);
        } catch (IOException e) {
            log.error("An error occurred during communicating with API", e);
        } catch (InvalidURLException e) {
            log.error("newScriptUrl is invalid ", e);
        }
    }
}
