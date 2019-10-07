package com.apidaze.sdk.client.calls;

import com.apidaze.sdk.client.credentials.Credentials;
import com.apidaze.sdk.client.messages.PhoneNumber;
import lombok.val;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;

import static com.apidaze.sdk.client.TestUtil.API_KEY;
import static com.apidaze.sdk.client.TestUtil.API_SECRET;
import static com.apidaze.sdk.client.calls.CallsRequest.create;
import static com.apidaze.sdk.client.calls.CallsResponse.ok;
import static org.assertj.core.api.Assertions.assertThat;

public class CallsClientTest {

    private static final int PORT = 9876;

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, PORT);

    private MockServerClient mockServer;

    private Calls client = CallsClient.builder()
            .baseUrl("http://localhost:" + PORT)
            .credentials(new Credentials(API_KEY, API_SECRET))
            .build();


    @Test
    public void shouldPlaceACallOfTypeNumber() {
        val callerId = PhoneNumber.of("14123456789");
        val origin = "48123456789";
        val destination = "48987654321";
        val callType = Calls.Type.NUMBER;

        val callId = "d64baf26-b116-4478-97b5-899de580461f";

        mockServer
                .when(create(callerId, origin, destination, callType))
                .respond(ok(callId));

        val response = client.create(callerId, origin, destination, callType);

        assertThat(response).isEqualTo(callId);
        mockServer.verify(create(callerId, origin, destination, callType));
    }

    @Test
    public void shouldPlaceACallOfTypeSipAccount() {
        val callerId = PhoneNumber.of("14123456789");
        val origin = "sip-account-origin";
        val destination = "sip-account-destination";
        val callType = Calls.Type.SIP_ACCOUNT;

        val callId = "d64baf26-b116-4478-97b5-899de580461f";

        mockServer
                .when(create(callerId, origin, destination, callType))
                .respond(ok(callId));

        val response = client.create(callerId, origin, destination, callType);

        assertThat(response).isEqualTo(callId);
        mockServer.verify(create(callerId, origin, destination, callType));
    }
}
