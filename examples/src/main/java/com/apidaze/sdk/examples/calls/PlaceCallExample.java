package com.apidaze.sdk.examples.calls;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.calls.Calls;
import com.apidaze.sdk.client.calls.CallsClient;
import com.apidaze.sdk.client.messages.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static java.util.Objects.isNull;

@Slf4j
public class PlaceCallExample {

    public static void main(String... args) {

        val apiKey = System.getenv("API_KEY");
        val apiSecret = System.getenv("API_SECRET");

        if (isNull(apiKey) || isNull(apiSecret)) {
            log.error("System environment variables API_KEY and API_SECRET must be set.");
            System.exit(1);
        }

        // initiate the client
        val calls = CallsClient.create(new Credentials(apiKey, apiSecret));

        // call details
        val callerId = PhoneNumber.of("14123456789");
        val origin = "48123456789";
        val destination = "48123456789";

        try {
            // place a call
            val callId = calls.create(callerId, origin, destination, Calls.Type.NUMBER);
            log.info("Call with id = {} has been initiated.", callId);
        } catch (WebClientResponseException e) {
            log.error("API returned the response with status code = [{}] and body = [{}]", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (CallsClient.ApiResponseException e) {
            log.error("Placing the call failed due to [{}].", e.getMessage());
        }
    }
}
