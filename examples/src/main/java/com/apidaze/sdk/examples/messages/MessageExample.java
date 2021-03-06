package com.apidaze.sdk.examples.messages;

import com.apidaze.sdk.client.ApplicationAction;
import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.common.InvalidPhoneNumberException;
import com.apidaze.sdk.client.common.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;

import static java.util.Objects.isNull;

@Slf4j
public class MessageExample {

    public static void main(String... args) {

        val apiKey = System.getenv("API_KEY");
        val apiSecret = System.getenv("API_SECRET");

        if (isNull(apiKey) || isNull(apiSecret)) {
            log.error("System environment variables API_KEY and API_SECRET must be set.");
            System.exit(1);
        }

        // initiate ApplicationAction
        val applicationAction = ApplicationAction.create(new Credentials(apiKey, apiSecret));

        // phone numbers and message body
        val from = "123456789";
        val to = "987654321";
        val messageBody = "Have a nice day!";

        try {
            // send a message
            val response = applicationAction.sendTextMessage(PhoneNumber.of(from), PhoneNumber.of(to), messageBody);
            log.info(response);
        } catch (IOException e) {
            log.error("An error occurred during communicating with API", e);
        } catch (InvalidPhoneNumberException e) {
            log.error("Phone number {} is invalid", e.getMessage());
        }
    }
}
