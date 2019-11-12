package com.apidaze.sdk.examples.externalscripts;

import com.apidaze.sdk.client.ApplicationAction;
import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.common.InvalidURLException;
import com.apidaze.sdk.client.common.URL;
import com.apidaze.sdk.client.http.HttpResponseException;
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

        // initiate ApplicationAction
        val applicationAction = ApplicationAction.create(new Credentials(apiKey, apiSecret));

        try {
            // create an external script
            val createdScript = applicationAction.createExternalScript(scriptName, URL.fromString(scriptUrl));
            log.info("Created {}", createdScript);
        } catch (HttpResponseException e) {
            log.error(e.toString());
        } catch (IOException e) {
            log.error("An IO error occurred during communicating with API", e);
        } catch (InvalidURLException e) {
            log.error("scriptUrl is invalid ", e);
        }
    }
}
