package com.apidaze.sdk.client.applications;

import com.apidaze.sdk.client.GenericRequest;
import lombok.Getter;
import lombok.val;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.apidaze.sdk.client.GenericResponse.list;
import static com.apidaze.sdk.client.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationsClientTest extends GenericRequest {

    private static final String APP_ID = "app_id";
    private static final String APP_NAME = "app_name";
    private static final String API_KEY = "api_key";

    @Getter
    private final String basePath = "applications";

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, PORT);

    private MockServerClient mockServer;

    private Applications client = ApplicationsClient.create(CREDENTIALS, BASE_URL);

    @Before
    public void setUp() {
        mockServer.reset();
    }

    @Test
    public void shouldReturnExternalScriptsList() throws IOException {
        val applications = applicationsList(3);

        mockServer
                .when(getAll())
                .respond(list(applications));

        val response = client.getApplications();

        assertThat(response)
                .usingComparatorForElementFieldsWithType(dateTimeComparator, ZonedDateTime.class)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(applications);

        mockServer.verify(getAll());
    }

    @Test
    public void shouldReturnApplicationById() throws IOException {
        val applications = applicationsList(1);
        val id = applications.get(0).getId();

        mockServer
                .when(getByParameter(APP_ID, id.toString()))
                .respond(list(applications));

        val response = client.getApplicationsById(id);

        assertThat(response)
                .usingComparatorForElementFieldsWithType(dateTimeComparator, ZonedDateTime.class)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(applications);

        mockServer.verify(getByParameter(APP_ID, id.toString()));
    }

    @Test
    public void shouldReturnApplicationByApiKey() throws IOException {
        val applications = applicationsList(1);
        val apiKey = applications.get(0).getApiKey();

        mockServer
                .when(getByParameter(API_KEY, apiKey))
                .respond(list(applications));

        val response = client.getApplicationsByApiKey(apiKey);

        assertThat(response)
                .usingComparatorForElementFieldsWithType(dateTimeComparator, ZonedDateTime.class)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(applications);

        mockServer.verify(getByParameter(API_KEY, apiKey));
    }

    @Test
    public void shouldReturnApplicationByName() throws IOException {
        val applications = applicationsList(1);
        val name = applications.get(0).getName();

        mockServer
                .when(getByParameter(APP_NAME, name))
                .respond(list(applications));

        val response = client.getApplicationsByName(name);

        assertThat(response)
                .usingComparatorForElementFieldsWithType(dateTimeComparator, ZonedDateTime.class)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(applications);

        mockServer.verify(getByParameter(APP_NAME, name));
    }

    private List<Application> applicationsList(int size) {
        return IntStream.range(0, size)
                .boxed()
                .map(i -> Application.builder()
                        .id(i.longValue())
                        .accountId(i.longValue())
                        .applicationId("applicationId" + i)
                        .apiKey("apiKey" + i)
                        .apiSecret("apiSecret" + i)
                        .name("name" + i)
                        .fsAddress("fsAddress" + i)
                        .createdAt(ZonedDateTime.parse("2019-10-09T12:23:21.000Z"))
                        .updatedAt(ZonedDateTime.parse("2019-10-11T15:00:04.000Z"))
                        .build())
                .collect(Collectors.toList());
    }
}
