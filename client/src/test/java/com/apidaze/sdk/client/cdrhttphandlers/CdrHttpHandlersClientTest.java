package com.apidaze.sdk.client.cdrhttphandlers;

import com.apidaze.sdk.client.GenericRequest;
import com.apidaze.sdk.client.common.URL;
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
import static com.apidaze.sdk.client.GenericResponse.one;
import static com.apidaze.sdk.client.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpResponse.response;

public class CdrHttpHandlersClientTest extends GenericRequest {

    @Getter
    private final String basePath = "cdrhttphandlers";

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, PORT);

    private MockServerClient mockServer;

    private CdrHttpHandlersClient client = CdrHttpHandlersClient.create(CREDENTIALS, BASE_URL);

    @Before
    public void setUp() {
        mockServer.reset();
    }

    @Test
    public void shouldReturnCdrHttpHandlersList() throws IOException {
        val cdrHttpHandlers = getCdrHttpHandlerList(3);

        mockServer
                .when(getAll())
                .respond(list(cdrHttpHandlers));

        val response = client.getCdrHttpHandlers();

        assertThat(response)
                .usingComparatorForElementFieldsWithType(dateTimeComparator, ZonedDateTime.class)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(cdrHttpHandlers);

        mockServer.verify(getAll());
    }

    @Test
    public void shouldCreateCdrHttpHandler() throws IOException {
        val cdrHttpHandler = getCdrHttpHandlerList(1).get(0);
        val name = cdrHttpHandler.getName();
        val url = cdrHttpHandler.getUrl();


        mockServer
                .when(create(name, url))
                .respond(one(cdrHttpHandler).withStatusCode(201));

        val response = client.createCdrHttpHandler(name, url);

        assertThat(response)
                .usingRecursiveComparison()
                .withComparatorForType(dateTimeComparator, ZonedDateTime.class)
                .isEqualTo(cdrHttpHandler);

        mockServer.verify(create(name, url));
    }

    @Test
    public void shouldUpdateCdrHttpHandlerNameAndUrl() throws IOException {
        val cdrHttpHandler = getCdrHttpHandlerList(1).get(0);
        val id = cdrHttpHandler.getId();
        val name = cdrHttpHandler.getName();
        val url = cdrHttpHandler.getUrl();

        mockServer
                .when(update(id, name, url))
                .respond(one(cdrHttpHandler).withStatusCode(202));

        val response = client.updateCdrHttpHandler(id, name, url);

        assertThat(response)
                .usingRecursiveComparison()
                .withComparatorForType(dateTimeComparator, ZonedDateTime.class)
                .isEqualTo(cdrHttpHandler);

        mockServer.verify(update(id, name, url));
    }

    @Test
    public void shouldUpdateCdrHttpHandlerName() throws IOException {
        val cdrHttpHandler = getCdrHttpHandlerList(1).get(0);
        val id = cdrHttpHandler.getId();
        val name = cdrHttpHandler.getName();

        mockServer
                .when(updateName(id, name))
                .respond(one(cdrHttpHandler).withStatusCode(202));

        val response = client.updateCdrHttpHandlerName(id, name);

        assertThat(response)
                .usingRecursiveComparison()
                .withComparatorForType(dateTimeComparator, ZonedDateTime.class)
                .isEqualTo(cdrHttpHandler);

        mockServer.verify(updateName(id, name));
    }

    @Test
    public void shouldUpdateCdrHttpHandlerUrl() throws IOException {
        val cdrHttpHandler = getCdrHttpHandlerList(1).get(0);
        val id = cdrHttpHandler.getId();
        val url = cdrHttpHandler.getUrl();

        mockServer
                .when(updateUrl(id, url))
                .respond(one(cdrHttpHandler).withStatusCode(202));

        val response = client.updateCdrHttpHandlerUrl(id, url);

        assertThat(response)
                .usingRecursiveComparison()
                .withComparatorForType(dateTimeComparator, ZonedDateTime.class)
                .isEqualTo(cdrHttpHandler);

        mockServer.verify(updateUrl(id, url));
    }

    @Test
    public void shouldDeleteCdrHttpHandler() throws IOException {
        val id = 1L;

        mockServer
                .when(delete(id))
                .respond(response().withStatusCode(204));

        client.deleteCdrHttpHandler(id);

        mockServer.verify(delete(id));
    }


    private List<CdrHttpHandler> getCdrHttpHandlerList(int size) {
        return IntStream.range(0, size)
                .boxed()
                .map(i -> CdrHttpHandler.builder()
                        .id(i.longValue())
                        .name("CdrHttpHandler - " + i)
                        .format(CdrHttpHandler.Format.REGULAR)
                        .url(URL.fromString("http://url-" + i + ".com"))
                        .callLeg(CdrHttpHandler.CallLeg.INBOUND)
                        .createdAt(ZonedDateTime.parse("2019-10-09T12:23:21.000Z"))
                        .updatedAt(ZonedDateTime.parse("2019-10-11T15:00:04.000Z"))
                        .build())
                .collect(Collectors.toList());
    }

}
