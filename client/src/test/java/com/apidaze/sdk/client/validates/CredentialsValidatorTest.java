package com.apidaze.sdk.client.validates;

import lombok.val;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static com.apidaze.sdk.client.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.Parameter.param;

public class CredentialsValidatorTest {

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, PORT);

    private MockServerClient mockServer;

    private CredentialsValidator validator = CredentialsValidator.create(CREDENTIALS, BASE_URL);

    @Before
    public void before() {
        mockServer.reset();
    }

    @Test
    public void shouldReturnTrue_ifApiReturnsStatusCodeOK() {
        val responseBody = "\"status\": { \"global\": \"Authentication succeeded\" }";

        mockServer
                .when(validateRequest())
                .respond(response(responseBody));

        val result = validator.validateCredentials();

        mockServer.verify(validateRequest());
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalse_ifApiReturnsStatusCode401() {
        mockServer
                .when(validateRequest())
                .respond(response().withStatusCode(401));

        val result = validator.validateCredentials();

        mockServer.verify(validateRequest());
        assertThat(result).isFalse();
    }

    @Test
    public void shouldReturnFalse_ifApiReturnsStatusCode404() {
        mockServer
                .when(validateRequest())
                .respond(response().withStatusCode(404));

        val result = validator.validateCredentials();

        mockServer.verify(validateRequest());
        assertThat(result).isFalse();
    }

    @Test
    public void shouldThrowWebClientResponseException_ifApiReturnsStatusCode500() {
        mockServer
                .when(validateRequest())
                .respond(response().withStatusCode(500));

        assertThatExceptionOfType(WebClientResponseException.InternalServerError.class)
                .isThrownBy(() -> validator.validateCredentials())
                .withMessage("500 Internal Server Error");
    }

    private static HttpRequest validateRequest() {
        return request()
                .withMethod("GET")
                .withPath("/" + API_KEY + "/validates")
                .withQueryStringParameters(param("api_secret", API_SECRET));
    }
}
