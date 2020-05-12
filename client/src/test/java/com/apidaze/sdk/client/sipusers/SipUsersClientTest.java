package com.apidaze.sdk.client.sipusers;

import com.apidaze.sdk.client.GenericRequest;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.netty.handler.codec.http.HttpMethod;
import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;

import java.io.IOException;
import java.time.ZonedDateTime;

import static com.apidaze.sdk.client.GenericResponse.list;
import static com.apidaze.sdk.client.GenericResponse.one;
import static com.apidaze.sdk.client.TestUtil.*;
import static io.netty.handler.codec.http.HttpMethod.POST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.Parameter.param;

public class SipUsersClientTest extends GenericRequest {

    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_EMAIL_ADDRESS = "email_address";
    private static final String PARAM_INTERNAL_CALLER_ID_NUMBER = "internal_caller_id_number";
    private static final String PARAM_EXTERNAL_CALLER_ID_NUMBER = "external_caller_id_number";
    private static final String PARAM_RESET_PASSWORD = "reset_password";
    private static final String STATUS = "status";
    private static final String PASSWORD = "password";

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
    public void shouldReturnSipUsersList() throws IOException {
        val scripts = ImmutableList.of(sipUser1, sipUser2, sipUser3);

        mockServer
                .when(getAll())
                .respond(list(scripts));

        val response = client.getSipUsers();

        assertThat(response)
                .usingComparatorForElementFieldsWithType(dateTimeComparator, ZonedDateTime.class)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(scripts);

        mockServer.verify(getAll());
    }

    @Test
    public void shouldCreateSipUser() throws IOException {
        val sipUser = sipUser1;
        val username = sipUser.getSip().getUsername();
        val name = sipUser.getName();
        val email = "sipUser@domain.com";
        val internalCallerIdNumber = sipUser.getCallerId().getInternalCallerIdNumber();
        val externalCallerIdNumber = sipUser.getCallerId().getOutboundCallerIdNumber();

        val params = ImmutableMap.of(
                PARAM_USERNAME, username,
                PARAM_NAME, name,
                PARAM_EMAIL_ADDRESS, email,
                PARAM_INTERNAL_CALLER_ID_NUMBER, internalCallerIdNumber,
                PARAM_EXTERNAL_CALLER_ID_NUMBER, externalCallerIdNumber);

        mockServer
                .when(create(params))
                .respond(one(sipUser).withStatusCode(201));

        val response = client.createSipUser(username, name, email, internalCallerIdNumber, externalCallerIdNumber);

        assertThat(response)
                .usingRecursiveComparison()
                .withComparatorForType(dateTimeComparator, ZonedDateTime.class)
                .isEqualTo(sipUser);

        mockServer.verify(create(params));
    }


    @Test
    public void shouldReturnSipUserById() throws IOException {
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

    @Test
    public void shouldUpdateSipUser() throws IOException {
        val sipUser = sipUser1;
        val id = sipUser.getId();
        val name = sipUser.getName();
        val internalCallerIdNumber = sipUser.getCallerId().getInternalCallerIdNumber();
        val externalCallerIdNumber = sipUser.getCallerId().getOutboundCallerIdNumber();

        val params = ImmutableMap.of(
                PARAM_NAME, name,
                PARAM_INTERNAL_CALLER_ID_NUMBER, internalCallerIdNumber,
                PARAM_EXTERNAL_CALLER_ID_NUMBER, externalCallerIdNumber);

        mockServer
                .when(update(id, params))
                .respond(one(sipUser).withStatusCode(202));

        val response = client.updateSipUser(id, name, internalCallerIdNumber, externalCallerIdNumber);

        assertThat(response)
                .usingRecursiveComparison()
                .withComparatorForType(dateTimeComparator, ZonedDateTime.class)
                .isEqualTo(sipUser);

        mockServer.verify(update(id, params));
    }

    @Test
    public void shouldUpdateSipUserAndResetPassword() throws IOException {
        val sipUser = sipUserWithPassword;
        val id = sipUser.getId();
        val name = sipUser.getName();
        val internalCallerIdNumber = sipUser.getCallerId().getInternalCallerIdNumber();
        val externalCallerIdNumber = sipUser.getCallerId().getOutboundCallerIdNumber();
        val resetPassword = true;

        val params = ImmutableMap.of(
                PARAM_NAME, name,
                PARAM_INTERNAL_CALLER_ID_NUMBER, internalCallerIdNumber,
                PARAM_EXTERNAL_CALLER_ID_NUMBER, externalCallerIdNumber,
                PARAM_RESET_PASSWORD, String.valueOf(resetPassword));

        mockServer
                .when(update(id, params))
                .respond(one(sipUser).withStatusCode(202));

        val response = client.updateSipUser(id, name, internalCallerIdNumber, externalCallerIdNumber, resetPassword);

        assertThat(response)
                .usingRecursiveComparison()
                .withComparatorForType(dateTimeComparator, ZonedDateTime.class)
                .isEqualTo(sipUser);

        mockServer.verify(update(id, params));
    }

    @Test
    public void shouldDeleteCdrHttpHandler() throws IOException {
        val id = 1L;

        mockServer
                .when(delete(id))
                .respond(response().withStatusCode(204));

        client.deleteSipUser(id);

        mockServer.verify(delete(id));
    }

    @Test
    public void shouldReturnSipUserStatus() throws IOException {
        val id = 1L;
        val status = new SipUserStatus("some uri", "Registered");

        mockServer
                .when(getSipUserStatus(id))
                .respond(one(status));

        val response = client.getSipUserStatus(id);

        assertThat(response).isPresent();
        assertThat(response.get()).isEqualTo(status);

        mockServer.verify(getSipUserStatus(id));
    }

    @Test
    public void getSipUserStatusShouldReturnEmptyResponse_ifApiReturns404() throws IOException {
        val id = 1L;

        mockServer
                .when(getSipUserStatus(id))
                .respond(response().withStatusCode(404));

        val response = client.getSipUserStatus(id);

        assertThat(response).isEmpty();
        mockServer.verify(getSipUserStatus(id));
    }


    @Test
    public void shouldResetSipUserPassword() throws IOException {
        val id = sipUserWithPassword.getId();

        mockServer
                .when(resetSipUserPassword(id))
                .respond(one(sipUserWithPassword));

        val response = client.resetSipUserPassword(id);

        assertThat(response).isPresent();
        assertThat(response.get())
                .usingRecursiveComparison()
                .withComparatorForType(dateTimeComparator, ZonedDateTime.class)
                .isEqualTo(sipUserWithPassword);

        mockServer.verify(resetSipUserPassword(id));
    }

    @Test
    public void resetSipUserPasswordShouldReturnEmptyResponse_ifApiReturns404() throws IOException {
        val id = 1L;

        mockServer
                .when(resetSipUserPassword(id))
                .respond(response().withStatusCode(404));

        val response = client.resetSipUserPassword(id);

        assertThat(response).isEmpty();
        mockServer.verify(resetSipUserPassword(id));
    }

    private HttpRequest getSipUserStatus(@NotNull Long id) {
        return request()
                .withMethod(HttpMethod.GET.name())
                .withPath("/" + API_KEY + "/" + getBasePath() + "/" + id + "/" + STATUS)
                .withQueryStringParameters(param(PARAM_API_SECRET, API_SECRET));
    }

    private HttpRequest resetSipUserPassword(@NotNull Long id) {
        return request()
                .withMethod(POST.name())
                .withPath("/" + API_KEY + "/" + getBasePath() + "/" + id + "/" + PASSWORD)
                .withQueryStringParameters(param(PARAM_API_SECRET, API_SECRET));
    }

    private final SipUser sipUser1 = SipUser.builder()
            .id(1L)
            .name("Anonymous")
            .callerId(
                    SipUser.CallerId.builder()
                            .internalCallerIdName("Anonymous")
                            .internalCallerIdNumber("1234")
                            .outboundCallerIdName("Anonymous")
                            .outboundCallerIdNumber("99999")
                            .build())
            .sip(SipUser.Sip.builder()
                    .username("anonymous")
                    .build())
            .createdAt(ZonedDateTime.parse("2019-09-19T11:20:18.123Z"))
            .updatedAt(ZonedDateTime.parse("2019-09-20T13:44:18.123Z"))
            .build();

    private final SipUser sipUser2 = SipUser.builder()
            .id(2L)
            .name("Chuck Norris")
            .callerId(
                    SipUser.CallerId.builder()
                            .internalCallerIdName("Chuck Norris")
                            .internalCallerIdNumber("4321")
                            .outboundCallerIdName("Chuck Norris")
                            .outboundCallerIdNumber("77777777")
                            .build())
            .sip(SipUser.Sip.builder()
                    .username("anonymous")
                    .build())
            .createdAt(ZonedDateTime.parse("2020-02-19T11:20:18.123Z"))
            .updatedAt(ZonedDateTime.parse("2020-02-20T13:44:18.123Z"))
            .build();

    private final SipUser sipUser3 = SipUser.builder()
            .id(3L)
            .name("The beast")
            .callerId(
                    SipUser.CallerId.builder()
                            .internalCallerIdName("The beast")
                            .internalCallerIdNumber("666")
                            .outboundCallerIdName("The beast")
                            .outboundCallerIdNumber("6666666")
                            .build())
            .sip(SipUser.Sip.builder()
                    .username("beast")
                    .build())
            .createdAt(ZonedDateTime.parse("1982-03-29T11:20:18.123Z"))
            .build();

    private final SipUser sipUserWithPassword = SipUser.builder()
            .id(999L)
            .name("Agent secret")
            .callerId(
                    SipUser.CallerId.builder()
                            .internalCallerIdName("Agent secret")
                            .internalCallerIdNumber("999")
                            .outboundCallerIdName("Agent secret")
                            .outboundCallerIdNumber("98762938746")
                            .build())
            .sip(SipUser.Sip.builder()
                    .username("agent_secret")
                    .password("secret")
                    .build())
            .createdAt(ZonedDateTime.parse("1982-03-29T11:20:18.123Z"))
            .build();
}
