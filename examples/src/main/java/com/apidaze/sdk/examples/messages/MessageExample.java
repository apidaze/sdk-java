package com.apidaze.sdk.examples.messages;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.messages.MessageClient;
import com.apidaze.sdk.client.messages.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
public class MessageExample {

    public static void main(String... args) {

        if (args.length < 2) {
            System.err.println("You must provide: <apiKey> <apiSecret> in the  argument list!");
            System.exit(1);
        }

        val apiKey = args[0];
        val apiSecret = args[1];

        // initiate the client
        val message = MessageClient.builder().credentials(new Credentials(apiKey, apiSecret)).build();

        try {
            // define phone numbers and message body
            val from = PhoneNumber.of("123456789");
            val to = PhoneNumber.of("987654321");
            val messageBody = "Have a nice day!";

            // send a message
            val response = message.send(from, to, messageBody);
            log.info(response);
        } catch (WebClientResponseException e) {
            log.error("API returned the response with status code = [{}] and body = [{}]", e.getStatusCode(), e.getResponseBodyAsString());
        }
    }
}
