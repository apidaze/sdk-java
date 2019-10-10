package com.apidaze.sdk.examples.messages;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.messages.MessageClient;
import com.apidaze.sdk.client.messages.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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

        // initiate the client
        val message = MessageClient.builder().credentials(new Credentials(apiKey, apiSecret)).build();

        // phone numbers and message body
        val from = PhoneNumber.of("123456789");
        val to = PhoneNumber.of("987654321");
        val messageBody = "Have a nice day!";

        try {
            // send a message
            val response = message.send(from, to, messageBody);
            log.info(response);
        } catch (WebClientResponseException e) {
            log.error("API returned the response with status code = [{}] and body = [{}]", e.getStatusCode(), e.getResponseBodyAsString());
        }
    }
}
