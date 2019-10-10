package com.apidaze.sdk.examples.calls;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.calls.CallsClient;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static java.util.Objects.isNull;

@Slf4j
public class ActiveCallsExample {

    public static void main(String... args) {

        val apiKey = System.getenv("API_KEY");
        val apiSecret = System.getenv("API_SECRET");

        if (isNull(apiKey) || isNull(apiSecret)) {
            log.error("System environment variables API_KEY and API_SECRET must be set.");
            System.exit(1);
        }

        // initiate the client
        val calls = CallsClient.create(new Credentials(apiKey, apiSecret));

        try {
            val response = calls.getActiveCalls();
            log.info("Active calls: {}", response);
        } catch (WebClientResponseException e) {
            log.error("API returned the response with status code = [{}] and body = [{}]", e.getStatusCode(), e.getResponseBodyAsString());
        }
    }
}
