package com.apidaze.sdk.client.validates;

import lombok.val;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;

import java.io.IOException;

import static com.apidaze.sdk.client.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIOException;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.Parameter.param;

public class CredentialsValidatorTest {

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, PORT);

    private MockServerClient mockServer;

    private CredentialsValidator validator = CredentialsValidatorClient.create(CREDENTIALS, BASE_URL);

    @Before
    public void before() {
        mockServer.reset();
    }

    @Test
    public void shouldReturnTrue_ifApiReturnsStatusCodeOK() throws IOException {
        val responseBody = "\"status\": { \"global\": \"Authentication succeeded\" }";

        mockServer
                .when(validateCredentialsRequest())
                .respond(response(responseBody));

        val result = validator.validateCredentials();

        mockServer.verify(validateCredentialsRequest());
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalse_ifApiReturnsStatusCode401() throws IOException {
        mockServer
                .when(validateCredentialsRequest())
                .respond(response().withStatusCode(401));

        val result = validator.validateCredentials();

        mockServer.verify(validateCredentialsRequest());
        assertThat(result).isFalse();
    }

    @Test
    public void shouldReturnFalse_ifApiReturnsStatusCode404AndSpecificErrorMessage() throws IOException {
        val responseBody = "{\"code\": \"E_NOT_FOUND\", \"message\": \"api secret and api key are not a pair\"}";

        mockServer
                .when(validateCredentialsRequest())
                .respond(response()
                        .withStatusCode(404)
                        .withBody(responseBody));

        val result = validator.validateCredentials();

        mockServer.verify(validateCredentialsRequest());
        assertThat(result).isFalse();
    }

    @Test
    public void shouldThrowIOException_ifApiReturnsStatusCode500() {
        mockServer
                .when(validateCredentialsRequest())
                .respond(response().withStatusCode(500));

        assertThatIOException()
                .isThrownBy(() -> validator.validateCredentials())
                .withMessageContainingAll("500", "Internal Server Error");
    }

    private static HttpRequest validateCredentialsRequest() {
        return request()
                .withMethod("GET")
                .withPath("/" + API_KEY + "/validates")
                .withQueryStringParameters(param("api_secret", API_SECRET));
    }
}
