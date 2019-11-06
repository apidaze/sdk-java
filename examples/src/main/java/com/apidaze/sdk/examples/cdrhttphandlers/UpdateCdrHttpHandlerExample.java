package com.apidaze.sdk.examples.cdrhttphandlers;

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
public class UpdateCdrHttpHandlerExample {

    public static void main(String... args) {

        val apiKey = System.getenv("API_KEY");
        val apiSecret = System.getenv("API_SECRET");

        if (isNull(apiKey) || isNull(apiSecret)) {
            log.error("System environment variables API_KEY and API_SECRET must be set.");
            System.exit(1);
        }

        val handlerId = 1L;
        val newHandlerName = "New handler";
        val newHandlerUrl = "http://new.handler.com";

        // initiate ApplicationAction
        val applicationAction = ApplicationAction.create(new Credentials(apiKey, apiSecret));

        try {
            // update CdrHttpHandler
            val result = applicationAction.updateCdrHttpHandler(handlerId, newHandlerName, URL.fromString(newHandlerUrl));
            log.info("Updated CdrHttpHandler: {}", result);
        } catch (HttpResponseException e) {
            log.error(e.toString());
        } catch (IOException e) {
            log.error("An IO error occurred during communicating with API", e);
        } catch (InvalidURLException e) {
            log.error("handlerUrl is invalid ", e);
        }
    }
}
