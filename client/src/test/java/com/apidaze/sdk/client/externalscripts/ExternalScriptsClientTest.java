package com.apidaze.sdk.client.externalscripts;

import com.apidaze.sdk.client.credentials.Credentials;
import com.google.common.collect.ImmutableList;
import lombok.val;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.ZonedDateTime;

import static com.apidaze.sdk.client.TestUtil.*;
import static com.apidaze.sdk.client.externalscripts.ExternalScriptsRequest.*;
import static com.apidaze.sdk.client.externalscripts.ExternalScriptsResponse.list;
import static com.apidaze.sdk.client.externalscripts.ExternalScriptsResponse.one;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockserver.model.HttpResponse.response;

public class ExternalScriptsClientTest {

    private static final int PORT = 9876;

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, PORT);

    private MockServerClient mockServer;

    private ExternalScripts client = ExternalScriptsClient.builder()
            .baseUrl("http://localhost:" + PORT)
            .credentials(new Credentials(API_KEY, API_SECRET))
            .build();

    private ExternalScript script1 = ExternalScript.builder()
            .id(1L)
            .name("The name of the first script")
            .url(URL.fromString("https://my.first.application.com"))
            .smsUrl(URL.fromString("https://my.first.sms.application.com"))
            .resellerCustomerId(5L)
            .devCustomerId(6L)
            .createdAt(ZonedDateTime.parse("2019-09-19T11:20:18.123Z"))
            .updatedAt(ZonedDateTime.parse("2019-09-20T13:44:18.123Z"))
            .build();

    private ExternalScript script2 = ExternalScript.builder()
            .id(2L)
            .name("The name of the second script")
            .url(URL.fromString("https://my.second.application.com"))
            .smsUrl(URL.fromString("https://my.second.sms.application.com"))
            .resellerCustomerId(12L)
            .devCustomerId(34L)
            .createdAt(ZonedDateTime.parse("2019-10-14T09:20:18.123Z"))
            .updatedAt(ZonedDateTime.parse("2019-12-09T13:44:18.123Z"))
            .build();

    @Before
    public void setUp() {
        mockServer.reset();
    }

    @Test
    public void shouldReturnExternalScriptsList() {
        val scripts = ImmutableList.of(script1, script2);

        mockServer
                .when(getAll())
                .respond(list(scripts));

        val response = client.list().collectList().block();

        assertThat(response)
                .usingComparatorForElementFieldsWithType(dateTimeComparator, ZonedDateTime.class)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(scripts);

        mockServer.verify(getAll());
    }

    @Test
    public void shouldReturnExternalScriptById() {
        val script = script1;

        mockServer
                .when(getById(script.getId()))
                .respond(one(script));

        val response = client.get(script.getId()).block();

        assertThat(response)
                .usingRecursiveComparison()
                .withComparatorForType(dateTimeComparator, ZonedDateTime.class)
                .isEqualTo(script);

        mockServer.verify(getById(script.getId()));
    }

    @Test
    public void shouldCreateExternalScript() {
        val script = script1;
        val scriptName = script.getName();
        val scriptUrl = script.getUrl();

        mockServer
                .when(create(scriptName, scriptUrl))
                .respond(one(script).withStatusCode(201));

        val response = client.create(scriptName, scriptUrl).block();

        assertThat(response)
                .usingRecursiveComparison()
                .withComparatorForType(dateTimeComparator, ZonedDateTime.class)
                .isEqualTo(script);

        mockServer.verify(create(scriptName, scriptUrl));
    }

    @Test
    public void shouldUpdateExternalScriptUrl() {
        val script = script1;
        val scriptId = script.getId();
        val scriptUrl = script.getUrl();

        mockServer
                .when(updateUrl(scriptId, scriptUrl))
                .respond(one(script).withStatusCode(202));

        val response = client.updateUrl(scriptId, scriptUrl).block();

        assertThat(response)
                .usingRecursiveComparison()
                .withComparatorForType(dateTimeComparator, ZonedDateTime.class)
                .isEqualTo(script);

        mockServer.verify(updateUrl(scriptId, scriptUrl));
    }

    @Test
    public void shouldUpdateExternalScriptUrlAndName() {
        val script = script1;
        val scriptId = script.getId();
        val scriptUrl = script.getUrl();
        val scriptName = script.getName();

        mockServer
                .when(update(scriptId, scriptName, scriptUrl))
                .respond(one(script).withStatusCode(202));

        val response = client.update(scriptId, scriptName, scriptUrl).block();

        assertThat(response)
                .usingRecursiveComparison()
                .withComparatorForType(dateTimeComparator, ZonedDateTime.class)
                .isEqualTo(script);

        mockServer.verify(update(scriptId, scriptName, scriptUrl));
    }

    @Test
    public void shouldNotInvokeApi_ifNameIsTooLongInCreateExternalScript() {
        val scriptName = "Very long name.................................";
        val scriptUrl = URL.fromString("http://my.script.com");

        assertThat(scriptName.length())
                .isGreaterThan(ExternalScriptsClient.MAX_NAME_LENGTH);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> client.create(scriptName, scriptUrl).block())
                .withMessageContaining("name: maximum");

        mockServer.verifyZeroInteractions();
    }

    @Test
    public void shouldNotInvokeApi_ifNameIsTooLongInUpdateExternalScript() {
        val scriptId = 0L;
        val scriptName = "Very long name.................................";
        val scriptUrl = URL.fromString("http://my.script.com");

        assertThat(scriptName.length())
                .isGreaterThan(ExternalScriptsClient.MAX_NAME_LENGTH);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> client.update(scriptId, scriptName, scriptUrl).block())
                .withMessageContaining("name: maximum");

        mockServer.verifyZeroInteractions();
    }

    @Test
    public void shouldThrowWebClientResponseException_ifApiReturnsAnError() {
        val script = script1;
        val scriptId = script.getId();
        val scriptUrl = script.getUrl();

        mockServer
                .when(updateUrl(scriptId, scriptUrl))
                .respond(response().withStatusCode(500));

        assertThatExceptionOfType(WebClientResponseException.InternalServerError.class)
                .isThrownBy(() -> client.updateUrl(scriptId, scriptUrl).block())
                .withMessage("500 Internal Server Error");
    }
}
