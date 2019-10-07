package com.apidaze.sdk.client.credentials;

import lombok.val;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;

import static com.apidaze.sdk.client.TestUtil.API_KEY;
import static com.apidaze.sdk.client.TestUtil.API_SECRET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.Parameter.param;

public class CredentialsValidatorTest {

    private static final int PORT = 9876;

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, PORT);

    private MockServerClient mockServer;

    private CredentialsValidator validator = CredentialsValidator.builder()
            .baseUrl("http://localhost:" + PORT)
            .credentials(new Credentials(API_KEY, API_SECRET))
            .build();


    @Before
    public void before() {
        mockServer.reset();
    }

    @Test
    public void validateShouldReturnResponseOK_ifCredentialsAreValid() {
        val responseBody = "\"status\": { \"global\": \"Authentication succeeded\" }";

        mockServer
                .when(validateRequest())
                .respond(response(responseBody));

        val response = validator.validateCredentials();

        assertThat(response).isEqualTo(responseBody);

        mockServer.verify(validateRequest());
    }

    private static HttpRequest validateRequest() {
        return request()
                .withMethod("GET")
                .withPath("/" + API_KEY + "/validates")
                .withQueryStringParameters(param("api_secret", API_SECRET));
    }
}
