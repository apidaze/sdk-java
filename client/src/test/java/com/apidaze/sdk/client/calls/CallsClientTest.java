package com.apidaze.sdk.client.calls;

import com.apidaze.sdk.client.GenericRequest;
import com.apidaze.sdk.client.common.PhoneNumber;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.val;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.apidaze.sdk.client.GenericResponse.list;
import static com.apidaze.sdk.client.GenericResponse.one;
import static com.apidaze.sdk.client.TestUtil.*;
import static com.apidaze.sdk.client.calls.CallsResponse.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockserver.model.HttpResponse.response;

public class CallsClientTest extends GenericRequest {

    @Getter
    private final String basePath = "calls";

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, PORT);

    private MockServerClient mockServer;

    private Calls client = CallsClient.create(CREDENTIALS, BASE_URL);

    @Test
    public void shouldPlaceACallOfTypeNumber() throws IOException {
        val callerId = PhoneNumber.of("14123456789");
        val origin = "48123456789";
        val destination = "48987654321";
        val callType = Calls.CallType.NUMBER;
        val callId = UUID.fromString("d64baf26-b116-4478-97b5-899de580461f");

        mockServer
                .when(create(callerId, origin, destination, callType))
                .respond(ok(callId.toString()));

        val result = client.createCall(callerId, origin, destination, callType);

        mockServer.verify(create(callerId, origin, destination, callType));
        assertThat(result).isEqualTo(callId);
    }

    @Test
    public void shouldPlaceACallOfTypeSipAccount() throws IOException {
        val callerId = PhoneNumber.of("14123456789");
        val origin = "sip-account-origin";
        val destination = "sip-account-destination";
        val callType = Calls.CallType.SIP_ACCOUNT;
        val callId = UUID.fromString("d64baf26-b116-4478-97b5-899de580461f");

        mockServer
                .when(create(callerId, origin, destination, callType))
                .respond(ok(callId.toString()));

        val result = client.createCall(callerId, origin, destination, callType);

        mockServer.verify(create(callerId, origin, destination, callType));
        assertThat(result).isEqualTo(callId);
    }

    @Test
    public void createShouldThrowCreateResponseException_ifApiReturnsBodyWithFailureMessage() {
        val callerId = PhoneNumber.of("14123456789");
        val origin = "48123456789";
        val destination = "48987654321";
        val callType = Calls.CallType.NUMBER;
        val failureMessage = "NORMAL TEMPORARY_FAILURE";

        mockServer
                .when(create(callerId, origin, destination, callType))
                .respond(failed(failureMessage));

        assertThatExceptionOfType(CallsClient.CreateResponseException.class)
                .isThrownBy(() -> client.createCall(callerId, origin, destination, callType))
                .withMessage(failureMessage);

        mockServer.verify(create(callerId, origin, destination, callType));
    }

    @Test
    public void createShouldThrowCreateResponseException_ifApiReturnsEmptyJson() {
        val callerId = PhoneNumber.of("14123456789");
        val origin = "48123456789";
        val destination = "48987654321";
        val callType = Calls.CallType.NUMBER;

        mockServer
                .when(create(callerId, origin, destination, callType))
                .respond(emptyJson());

        assertThatExceptionOfType(CallsClient.CreateResponseException.class)
                .isThrownBy(() -> client.createCall(callerId, origin, destination, callType))
                .withMessage("missing call id in the response body");

        mockServer.verify(create(callerId, origin, destination, callType));
    }


    @Test
    public void shouldDeleteCall() throws IOException {
        val id = UUID.fromString("d64baf26-b116-4478-97b5-899de580461f");

        mockServer
                .when(delete(id.toString()))
                .respond(ok(""));

        client.deleteCall(id);

        mockServer.verify(delete(id.toString()));
    }

    @Test
    public void deleteCallShouldThrowDeleteResponseException_ifApiReturnsBodyWithFailureMessage() {
        val id = UUID.fromString("d64baf26-b116-4478-97b5-899de580461f");
        val failureMessage = "NORMAL TEMPORARY_FAILURE";

        mockServer
                .when(delete(id.toString()))
                .respond(failed(failureMessage));

        assertThatExceptionOfType(CallsClient.DeleteResponseException.class)
                .isThrownBy(() -> client.deleteCall(id))
                .withMessage(failureMessage);
    }

    @Test
    public void shouldReturnListOfCalls() throws IOException {
        val calls = generateCallsList();

        mockServer
                .when(getAll())
                .respond(list(calls));

        val result = client.getCalls();

        mockServer.verify(getAll());
        assertThat(result)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(calls);
    }

    @Test
    public void shouldReturnCallById() throws IOException {
        val call = generateCallsList().get(0);
        val id = call.getUuid();

        mockServer
                .when(getById(id))
                .respond(one(call));

        val result = client.getCall(id);

        mockServer.verify(getById(id));

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(call);
    }

    @Test
    public void getCallShouldReturnEmpty_ifApiReturnsNotFound() throws IOException {
        val id = UUID.randomUUID();

        mockServer
                .when(getById(id))
                .respond(response().withStatusCode(404));

        val response = client.getCall(id);

        assertThat(response).isEmpty();
        mockServer.verify(getById(id));
    }

    private List<Call> generateCallsList() {
        return ImmutableList.of(
                Call.builder()
                        .uuid(UUID.fromString("cd79587d-c71e-4bb0-9fdc-244bf9a95538"))
                        .created(LocalDateTime.parse("2019-10-09T12:01:22"))
                        .callerIdName("Outbound Call")
                        .callerIdNumber("123456789")
                        .destination("123456789")
                        .callState(Calls.CallState.ACTIVE)
                        .callUuid("cd79587d-c71e-4bb0-9fdc-244bf9a95538")
                        .build(),
                Call.builder()
                        .uuid(UUID.fromString("cd79587d-c71e-4bb0-9fdc-244bf9a95538"))
                        .created(LocalDateTime.parse("2019-11-09T12:01:22"))
                        .callerIdName("Outbound Call")
                        .callerIdNumber("987654321")
                        .destination("987654321")
                        .callState(Calls.CallState.EARLY)
                        .callUuid("fa67a5f3-bac4-48bb-ade7-efa19cd99938")
                        .build()
        );
    }

    private HttpRequest create(PhoneNumber callerId, String origin, String destination, Calls.CallType callType) {
        return create(ImmutableMap.of(
                "callerid", callerId.getNumber(),
                "origin", origin,
                "destination", destination,
                "type", callType.getValue()
        ));
    }
}
