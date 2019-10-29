package com.apidaze.sdk.client.cdrhttphandlers;

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

import static com.apidaze.sdk.client.TestUtil.*;
import static com.apidaze.sdk.client.cdrhttphandlers.CdrHttpHandlersRequest.getAll;
import static com.apidaze.sdk.client.cdrhttphandlers.CdrHttpHandlersResponse.list;
import static org.assertj.core.api.Assertions.assertThat;

public class CdrHttpHandlersClientTest {

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, PORT);

    private MockServerClient mockServer;

    private CdrHttpHandlersClient client = CdrHttpHandlersClient.create(CREDENTIALS, BASE_URL);

    @Before
    public void setUp() {
        mockServer.reset();
    }

    @Test
    public void shouldReturnExternalScriptsList() throws IOException {
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

    private List<CdrHttpHandler> getCdrHttpHandlerList(int size) {
        return IntStream.range(0, size - 1)
                .boxed()
                .map(i -> CdrHttpHandler.builder()
                        .id(i.longValue())
                        .name("CdrHttpHandler - " + i)
                        .format("regular")
                        .url("http://url-" + i + ".com")
                        .callLeg("inbound")
                        .createdAt(ZonedDateTime.parse("2019-10-09T12:23:21.000Z"))
                        .updatedAt(ZonedDateTime.parse("2019-10-11T15:00:04.000Z"))
                        .build())
                .collect(Collectors.toList());
    }

}
