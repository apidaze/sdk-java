package com.apidaze.sdk.examples.calls;

import com.apidaze.sdk.client.ApplicationAction;
import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.calls.Calls;
import com.apidaze.sdk.client.calls.CallsClient;
import com.apidaze.sdk.client.common.InvalidPhoneNumberException;
import com.apidaze.sdk.client.common.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.util.Objects.isNull;

@Slf4j
public class CallsAllInOneExample {

    public static void main(String... args) {

        val apiKey = System.getenv("API_KEY");
        val apiSecret = System.getenv("API_SECRET");

        if (isNull(apiKey) || isNull(apiSecret)) {
            log.error("System environment variables API_KEY and API_SECRET must be set.");
            System.exit(1);
        }

        // initiate ApplicationAction
        val applicationAction = ApplicationAction.create(new Credentials(apiKey, apiSecret));

        // call details
        val callerId = "14123456789";
        val origin = "48123456789";
        val destination = "48123456789";

        try {
            // place a call
            val callId = applicationAction.createCall(PhoneNumber.of(callerId), origin, destination, Calls.CallType.NUMBER);
            log.info("Call with id = {} has been initiated.", callId);

            // get call details
            val call = applicationAction.getCall(callId);
            if (call.isPresent()) {
                log.info("Initiated call details = {}", call);
            } else {
                log.warn("There is no call with id = {}", callId);
            }

            // get full list of calls for your domain
            val calls = applicationAction.getCalls();
            log.info("Calls list = {}", calls);

            // hung up the call
            log.info("Press RETURN to hung up the call...");
            new BufferedReader(new InputStreamReader(System.in)).readLine();

            applicationAction.deleteCall(callId);
            log.info("Call with id = {} has been deleted.", callId);

        } catch (IOException e) {
            log.error("IOException occurred", e);
        } catch (CallsClient.CreateResponseException e) {
            log.error("Placing the call failed due to [{}].", e.getMessage());
        } catch (CallsClient.DeleteResponseException e) {
            log.error("Deleting the call failed due to [{}].", e.getMessage());
        } catch (InvalidPhoneNumberException e) {
            log.error("Phone number {} is invalid", e.getMessage());
        }
    }
}
