package com.apidaze.sdk.client.sipusers;

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

import static com.apidaze.sdk.client.GenericResponse.one;
import static com.apidaze.sdk.client.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SipUsersClientTest extends GenericRequest {

    @Getter
    private final String basePath = "sipusers";

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, PORT);

    private MockServerClient mockServer;

    private SipUsers client = SipUsersClient.create(CREDENTIALS, BASE_URL);

    @Before
    public void setUp() {
        mockServer.reset();
    }


    @Test
    public void shouldReturnExternalScriptById() throws IOException {
        val sipUser = sipUser1;
        val id = sipUser.getId();

        mockServer
                .when(getById(id))
                .respond(one(sipUser));

        val response = client.getSipUser(id);

        assertThat(response).isPresent();
        assertThat(response.get())
                .usingRecursiveComparison()
                .withComparatorForType(dateTimeComparator, ZonedDateTime.class)
                .isEqualTo(sipUser);

        mockServer.verify(getById(id));
    }


    private final SipUser sipUser1 = SipUser.builder()
            .id(1L)
            .name("Ali")
            .callerId(
                    SipUser.CallerId.builder()
                            .internalCallerIdName("Ali")
                            .internalCallerIdNumber("1234")
                            .outboundCallerIdName("Ali")
                            .outboundCallerIdNumber("99999")
                            .build())
            .createdAt(ZonedDateTime.parse("2019-09-19T11:20:18.123Z"))
            .updatedAt(ZonedDateTime.parse("2019-09-20T13:44:18.123Z"))
            .build();
}
