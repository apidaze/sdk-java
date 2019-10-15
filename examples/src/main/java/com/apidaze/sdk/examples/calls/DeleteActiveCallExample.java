package com.apidaze.sdk.examples.calls;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.calls.CallsClient;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;
import java.util.UUID;

import static java.util.Objects.isNull;

@Slf4j
public class DeleteActiveCallExample {

    public static void main(String... args) {

        val apiKey = System.getenv("API_KEY");
        val apiSecret = System.getenv("API_SECRET");

        if (isNull(apiKey) || isNull(apiSecret)) {
            log.error("System environment variables API_KEY and API_SECRET must be set.");
            System.exit(1);
        }

        // initiate the client
        val calls = CallsClient.create(new Credentials(apiKey, apiSecret));

        // call id to be deleted
        val callId = UUID.fromString("3691360b-412c-4a9a-aea7-e9f089851c8b");

        // delete a call
        try {
            calls.deleteActiveCall(callId);
            log.info("Call with id = {} has been deleted.", callId);
        } catch (IOException e) {
            log.error("An error occurred during communicating with API", e);
        } catch (CallsClient.DeleteResponseException e) {
            log.error("Deleting the call failed due to [{}].", e.getMessage());
        }
    }
}
