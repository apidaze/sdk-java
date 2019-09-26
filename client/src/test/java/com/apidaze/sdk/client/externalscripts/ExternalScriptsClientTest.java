package com.apidaze.sdk.client.externalscripts;

import com.apidaze.sdk.client.AbstractClientTest;
import com.apidaze.sdk.client.credentials.Credentials;
import com.google.common.collect.ImmutableList;
import lombok.val;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;

import java.time.ZonedDateTime;

import static com.apidaze.sdk.client.externalscripts.ExternalScriptsRequest.*;
import static com.apidaze.sdk.client.externalscripts.ExternalScriptsResponse.list;
import static com.apidaze.sdk.client.externalscripts.ExternalScriptsResponse.one;
import static org.assertj.core.api.Assertions.assertThat;

public class ExternalScriptsClientTest extends AbstractClientTest {

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
            .url("https://url.of.first.application")
            .smsUrl("https://url.of.first.sms.application")
            .resellerCustomerId(5L)
            .devCustomerId(6L)
            .createdAt(ZonedDateTime.parse("2019-09-19T11:20:18.123Z"))
            .updatedAt(ZonedDateTime.parse("2019-09-20T13:44:18.123Z"))
            .build();

    private ExternalScript script2 = ExternalScript.builder()
            .id(2L)
            .name("The name of the second script")
            .url("https://url.of.second.application")
            .smsUrl("https://url.of.second.sms.application")
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
        val scriptToBeReturned = script1;

        mockServer
                .when(getById(scriptToBeReturned.getId()))
                .respond(one(scriptToBeReturned).withStatusCode(200));

        val response = client.get(scriptToBeReturned.getId()).block();

        assertThat(response)
                .usingRecursiveComparison()
                .withComparatorForType(dateTimeComparator, ZonedDateTime.class)
                .isEqualTo(scriptToBeReturned);

        mockServer.verify(getById(scriptToBeReturned.getId()));
    }

    @Test
    public void shouldCreateExternalScript() {
        val scriptToBeCreated = script1;
        val scriptName = scriptToBeCreated.getName();
        val scriptUrl = scriptToBeCreated.getUrl();

        mockServer
                .when(create(scriptName, scriptUrl))
                .respond(one(scriptToBeCreated).withStatusCode(201));

        val response = client.create(scriptName, scriptUrl).block();

        assertThat(response)
                .usingRecursiveComparison()
                .withComparatorForType(dateTimeComparator, ZonedDateTime.class)
                .isEqualTo(scriptToBeCreated);

        mockServer.verify(create(scriptName, scriptUrl));
    }

}
