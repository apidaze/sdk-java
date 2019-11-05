package com.apidaze.sdk.client.externalscripts;

import com.apidaze.sdk.client.GenericRequest;
import com.apidaze.sdk.client.common.URL;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.val;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;

import java.io.IOException;
import java.time.ZonedDateTime;

import static com.apidaze.sdk.client.GenericResponse.list;
import static com.apidaze.sdk.client.GenericResponse.one;
import static com.apidaze.sdk.client.TestUtil.*;
import static com.apidaze.sdk.client.externalscripts.ExternalScriptsResponse.ok;
import static org.assertj.core.api.Assertions.*;
import static org.mockserver.model.HttpResponse.response;

public class ExternalScriptsClientTest extends GenericRequest {

    @Getter
    private final String basePath = "externalscripts";

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, PORT);

    private MockServerClient mockServer;

    private ExternalScripts client = ExternalScriptsClient.create(CREDENTIALS, BASE_URL);

    @Before
    public void setUp() {
        mockServer.reset();
    }

    @Test
    public void shouldReturnExternalScriptsList() throws IOException {
        val scripts = ImmutableList.of(script1, script2);

        mockServer
                .when(getAll())
                .respond(list(scripts));

        val response = client.getExternalScripts();

        assertThat(response)
                .usingComparatorForElementFieldsWithType(dateTimeComparator, ZonedDateTime.class)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(scripts);

        mockServer.verify(getAll());
    }

    @Test
    public void shouldReturnExternalScriptById() throws IOException {
        val script = script1;
        val id = script.getId();

        mockServer
                .when(getById(id))
                .respond(one(script));

        val response = client.getExternalScript(id);

        assertThat(response).isPresent();
        assertThat(response.get())
                .usingRecursiveComparison()
                .withComparatorForType(dateTimeComparator, ZonedDateTime.class)
                .isEqualTo(script);

        mockServer.verify(getById(id));
    }

    @Test
    public void getExternalScriptByIdShouldReturnEmpty_ifApiReturnsNotFound() throws IOException {
        val id = 1L;

        mockServer
                .when(getById(id))
                .respond(response().withStatusCode(404));

        val response = client.getExternalScript(id);

        assertThat(response).isEmpty();
        mockServer.verify(getById(id));
    }


    @Test
    public void shouldCreateExternalScript() throws IOException {
        val script = script1;
        val scriptName = script.getName();
        val scriptUrl = script.getUrl();

        mockServer
                .when(create(scriptName, scriptUrl))
                .respond(one(script).withStatusCode(201));

        val response = client.createExternalScript(scriptName, scriptUrl);

        assertThat(response)
                .usingRecursiveComparison()
                .withComparatorForType(dateTimeComparator, ZonedDateTime.class)
                .isEqualTo(script);

        mockServer.verify(create(scriptName, scriptUrl));
    }

    @Test
    public void shouldDeleteExternalScript() throws IOException {
        val id = 1L;

        mockServer
                .when(delete(id))
                .respond(ok(""));

        client.deleteExternalScript(id);

        mockServer.verify(delete(id));
    }

    @Test
    public void shouldUpdateExternalScriptUrl() throws IOException {
        val script = script1;
        val scriptId = script.getId();
        val scriptUrl = script.getUrl();

        mockServer
                .when(updateUrl(scriptId, scriptUrl))
                .respond(one(script).withStatusCode(202));

        val response = client.updateExternalScriptUrl(scriptId, scriptUrl);

        assertThat(response)
                .usingRecursiveComparison()
                .withComparatorForType(dateTimeComparator, ZonedDateTime.class)
                .isEqualTo(script);

        mockServer.verify(updateUrl(scriptId, scriptUrl));
    }

    @Test
    public void shouldUpdateExternalScriptUrlAndName() throws IOException {
        val script = script1;
        val scriptId = script.getId();
        val scriptUrl = script.getUrl();
        val scriptName = script.getName();

        mockServer
                .when(update(scriptId, scriptName, scriptUrl))
                .respond(one(script).withStatusCode(202));

        val response = client.updateExternalScript(scriptId, scriptName, scriptUrl);

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

        assertThatIllegalArgumentException()
                .isThrownBy(() -> client.createExternalScript(scriptName, scriptUrl))
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

        assertThatIllegalArgumentException()
                .isThrownBy(() -> client.updateExternalScript(scriptId, scriptName, scriptUrl))
                .withMessageContaining("name: maximum");

        mockServer.verifyZeroInteractions();
    }

    @Test
    public void shouldThrowIOException_ifApiReturnsAnError() {
        val script = script1;
        val scriptId = script.getId();
        val scriptUrl = script.getUrl();

        mockServer
                .when(updateUrl(scriptId, scriptUrl))
                .respond(response().withStatusCode(500));

        assertThatIOException()
                .isThrownBy(() -> client.updateExternalScriptUrl(scriptId, scriptUrl))
                .withMessageContainingAll("500", "Internal Server Error");
    }

    private final ExternalScript script1 = ExternalScript.builder()
            .id(1L)
            .name("The name of the first script")
            .url(URL.fromString("https://my.first.application.com"))
            .smsUrl(URL.fromString("https://my.first.sms.application.com"))
            .resellerCustomerId(5L)
            .devCustomerId(6L)
            .createdAt(ZonedDateTime.parse("2019-09-19T11:20:18.123Z"))
            .updatedAt(ZonedDateTime.parse("2019-09-20T13:44:18.123Z"))
            .build();

    private final ExternalScript script2 = ExternalScript.builder()
            .id(2L)
            .name("The name of the second script")
            .url(URL.fromString("https://my.second.application.com"))
            .smsUrl(URL.fromString("https://my.second.sms.application.com"))
            .resellerCustomerId(12L)
            .devCustomerId(34L)
            .createdAt(ZonedDateTime.parse("2019-10-14T09:20:18.123Z"))
            .updatedAt(ZonedDateTime.parse("2019-12-09T13:44:18.123Z"))
            .build();

}
