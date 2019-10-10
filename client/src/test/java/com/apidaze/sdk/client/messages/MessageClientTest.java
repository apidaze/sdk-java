package com.apidaze.sdk.client.messages;

import lombok.val;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;

import java.io.IOException;

import static com.apidaze.sdk.client.TestUtil.*;
import static com.apidaze.sdk.client.messages.MessageRequest.send;
import static com.apidaze.sdk.client.messages.MessageResponse.ok;
import static org.assertj.core.api.Assertions.*;
import static org.mockserver.model.HttpResponse.response;

public class MessageClientTest {

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, PORT);

    private MockServerClient mockServer;

    private Message client = MessageClient.create(CREDENTIALS, BASE_URL);

    @Before
    public void setUp() {
        mockServer.reset();
    }

    @Test
    public void shouldSendMessage() throws IOException {
        val from = PhoneNumber.of("123456789");
        val to = PhoneNumber.of("987654321");
        val messageBody = "Have a nice day!";
        val responseBody = "{\"ok\": true, \"message\": \"SMS sent\" }";

        mockServer
                .when(send(from, to, messageBody))
                .respond(ok(responseBody));

        val response = client.send(from, to, messageBody);

        assertThat(response).isEqualTo(responseBody);

        mockServer.verify(send(from, to, messageBody));
    }

    @Test
    public void shouldNotInvokeApi_ifBodyIsEmpty() {
        val from = PhoneNumber.of("123456789");
        val to = PhoneNumber.of("987654321");
        val emptyBody = "";

        assertThatIllegalArgumentException()
                .isThrownBy(() -> client.send(from, to, emptyBody))
                .withMessage("body must not be null or empty");

        mockServer.verifyZeroInteractions();
    }

    @Test
    public void shouldThrowIOException_ifApiReturnsAnError() {
        val from = PhoneNumber.of("123456789");
        val to = PhoneNumber.of("987654321");
        val messageBody = "Have a nice day!";

        mockServer
                .when(send(from, to, messageBody))
                .respond(response().withStatusCode(500));

        assertThatIOException()
                .isThrownBy(() -> client.send(from, to, messageBody))
                .withMessageContainingAll("500", "Internal Server Error");
    }
}
