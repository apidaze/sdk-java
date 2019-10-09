package com.apidaze.sdk.client.messages;

import com.apidaze.sdk.client.base.Credentials;
import lombok.val;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static com.apidaze.sdk.client.TestUtil.API_KEY;
import static com.apidaze.sdk.client.TestUtil.API_SECRET;
import static com.apidaze.sdk.client.messages.MessageRequest.send;
import static com.apidaze.sdk.client.messages.MessageResponse.ok;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockserver.model.HttpResponse.response;

public class MessageClientTest {

    private static final int PORT = 9876;

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, PORT);

    private MockServerClient mockServer;

    private Message client = MessageClient.builder()
            .baseUrl("http://localhost:" + PORT)
            .credentials(new Credentials(API_KEY, API_SECRET))
            .build();

    @Before
    public void setUp() {
        mockServer.reset();
    }

    @Test
    public void shouldSendMessage() {
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

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> client.send(from, to, emptyBody))
                .withMessage("body must not be empty");

        mockServer.verifyZeroInteractions();
    }

    @Test
    public void shouldThrowWebClientResponseException_ifApiReturnsAnError() {
        val from = PhoneNumber.of("123456789");
        val to = PhoneNumber.of("987654321");
        val messageBody = "Have a nice day!";

        mockServer
                .when(send(from, to, messageBody))
                .respond(response().withStatusCode(500));

        assertThatExceptionOfType(WebClientResponseException.InternalServerError.class)
                .isThrownBy(() -> client.send(from, to, messageBody))
                .withMessage("500 Internal Server Error");
    }
}
