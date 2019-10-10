package com.apidaze.sdk.client.calls;

import com.apidaze.sdk.client.messages.PhoneNumber;
import com.google.common.collect.ImmutableList;
import lombok.val;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.apidaze.sdk.client.TestUtil.*;
import static com.apidaze.sdk.client.calls.CallsRequest.*;
import static com.apidaze.sdk.client.calls.CallsResponse.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CallsClientTest {

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, PORT);

    private MockServerClient mockServer;

    private Calls client = CallsClient.create(CREDENTIALS, BASE_URL);

    @Test
    public void shouldPlaceACallOfTypeNumber() throws IOException {
        val callerId = PhoneNumber.of("14123456789");
        val origin = "48123456789";
        val destination = "48987654321";
        val callType = Calls.Type.NUMBER;
        val callId = UUID.fromString("d64baf26-b116-4478-97b5-899de580461f");

        mockServer
                .when(create(callerId, origin, destination, callType))
                .respond(ok(callId.toString()));

        val result = client.create(callerId, origin, destination, callType);

        mockServer.verify(create(callerId, origin, destination, callType));
        assertThat(result).isEqualTo(callId);
    }

    @Test
    public void shouldPlaceACallOfTypeSipAccount() throws IOException {
        val callerId = PhoneNumber.of("14123456789");
        val origin = "sip-account-origin";
        val destination = "sip-account-destination";
        val callType = Calls.Type.SIP_ACCOUNT;
        val callId = UUID.fromString("d64baf26-b116-4478-97b5-899de580461f");

        mockServer
                .when(create(callerId, origin, destination, callType))
                .respond(ok(callId.toString()));

        val result = client.create(callerId, origin, destination, callType);

        mockServer.verify(create(callerId, origin, destination, callType));
        assertThat(result).isEqualTo(callId);
    }

    @Test
    public void createShouldThrowApiResponseException_ifApiReturnsBodyWithFailureMessage() {
        val callerId = PhoneNumber.of("14123456789");
        val origin = "48123456789";
        val destination = "48987654321";
        val callType = Calls.Type.NUMBER;
        val failureMessage = "NORMAL TEMPORARY_FAILURE";

        mockServer
                .when(create(callerId, origin, destination, callType))
                .respond(failed(failureMessage));

        assertThatExceptionOfType(CallsClient.ApiResponseException.class)
                .isThrownBy(() -> client.create(callerId, origin, destination, callType))
                .withMessage(failureMessage);

        mockServer.verify(create(callerId, origin, destination, callType));
    }

    @Test
    public void createShouldThrowApiResponseException_ifApiReturnsEmptyJson() {
        val callerId = PhoneNumber.of("14123456789");
        val origin = "48123456789";
        val destination = "48987654321";
        val callType = Calls.Type.NUMBER;

        mockServer
                .when(create(callerId, origin, destination, callType))
                .respond(emptyJson());

        assertThatExceptionOfType(CallsClient.ApiResponseException.class)
                .isThrownBy(() -> client.create(callerId, origin, destination, callType))
                .withMessage("missing call id in the response body");

        mockServer.verify(create(callerId, origin, destination, callType));
    }


    @Test
    public void shouldDeleteActiveCall() throws IOException {
        val id = UUID.fromString("d64baf26-b116-4478-97b5-899de580461f");

        mockServer
                .when(delete(id))
                .respond(ok(""));

        client.deleteActiveCall(id);

        mockServer.verify(delete(id));
    }

    @Test
    public void deleteActiveCallShouldThrowApiResponseException_ifApiReturnsBodyWithFailureMessage() {
        val id = UUID.fromString("d64baf26-b116-4478-97b5-899de580461f");
        val failureMessage = "NORMAL TEMPORARY_FAILURE";

        mockServer
                .when(delete(id))
                .respond(failed(failureMessage));

        assertThatExceptionOfType(CallsClient.ApiResponseException.class)
                .isThrownBy(() -> client.deleteActiveCall(id))
                .withMessage(failureMessage);
    }

    @Test
    public void shouldReturnListOfActiveCalls() throws IOException {
        val activeCalls = generateActiveCallsList();

        mockServer
                .when(getActiveCalls())
                .respond(list(activeCalls));

        val result = client.getActiveCalls();

        mockServer.verify(getActiveCalls());
        assertThat(result)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(activeCalls);
    }

    @Test
    public void shouldReturnActiveCallById() throws IOException {
        val activeCall = generateActiveCallsList().get(0);
        val id = activeCall.getUuid();

        mockServer
                .when(getActiveCall(id))
                .respond(one(activeCall));

        val result = client.getActiveCall(id);

        mockServer.verify(getActiveCall(id));
        assertThat(result).isEqualTo(activeCall);
    }

    private List<ActiveCall> generateActiveCallsList() {
        return ImmutableList.of(
                ActiveCall.builder()
                        .uuid(UUID.fromString("cd79587d-c71e-4bb0-9fdc-244bf9a95538"))
                        .created(LocalDateTime.parse("2019-10-09T12:01:22"))
                        .callerIdName("Outbound Call")
                        .callerIdNumber("123456789")
                        .destination("123456789")
                        .callState(Calls.State.ACTIVE)
                        .callUuid("cd79587d-c71e-4bb0-9fdc-244bf9a95538")
                        .build(),
                ActiveCall.builder()
                        .uuid(UUID.fromString("cd79587d-c71e-4bb0-9fdc-244bf9a95538"))
                        .created(LocalDateTime.parse("2019-11-09T12:01:22"))
                        .callerIdName("Outbound Call")
                        .callerIdNumber("987654321")
                        .destination("987654321")
                        .callState(Calls.State.ACTIVE)
                        .callUuid("fa67a5f3-bac4-48bb-ade7-efa19cd99938")
                        .build()
        );
    }
}
