package com.apidaze.sdk.client.sipusers;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.http.HttpResponseException;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class SipUsersClient extends BaseApiClient<SipUser> implements SipUsers {

    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_EMAIL_ADDRESS = "email_address";
    private static final String PARAM_INTERNAL_CALLER_ID_NUMBER = "internal_caller_id_number";
    private static final String PARAM_EXTERNAL_CALLER_ID_NUMBER = "external_caller_id_number";
    private static final String PARAM_RESET_PASSWORD = "reset_password";

    @Getter
    private final String basePath = "sipusers";
    @Getter
    private final Credentials credentials;
    @Getter
    private final String baseUrl;

    public static SipUsersClient create(@NotNull Credentials credentials) {
        return create(credentials, DEFAULT_BASE_URL);
    }

    static SipUsersClient create(@NotNull Credentials credentials, @NotNull String baseUrl) {
        requireNonNull(credentials, "Credentials must not be null.");
        requireNonNull(baseUrl, "baseUrl must not be null.");

        return new SipUsersClient(credentials, baseUrl);
    }

    @Override
    public List<SipUser> getSipUsers() throws IOException {
        return findAll(SipUser.class);
    }

    @Override
    public SipUser createSipUser(String username,
                                 String name,
                                 String emailAddress,
                                 String internalCallerIdNumber,
                                 String externalCallerIdNumber) throws IOException {
        requireNonNull(username, "username must not be null");
        val params = ImmutableMap.of(
                PARAM_USERNAME, username,
                PARAM_NAME, name,
                PARAM_EMAIL_ADDRESS, emailAddress,
                PARAM_INTERNAL_CALLER_ID_NUMBER, internalCallerIdNumber,
                PARAM_EXTERNAL_CALLER_ID_NUMBER, externalCallerIdNumber);
        return create(params, SipUser.class);
    }

    @Override
    public Optional<SipUser> getSipUser(Long id) throws IOException {
        requireNonNull(id, "id must not be null");
        return findById(id.toString(), SipUser.class);
    }

    @Override
    public SipUser updateSipUser(Long id,
                                 String name,
                                 String internalCallerIdNumber,
                                 String externalCallerIdNumber) throws IOException {
        return updateSipUser(id, name, internalCallerIdNumber, externalCallerIdNumber, false);
    }

    @Override
    public SipUser updateSipUser(Long id,
                                 String name,
                                 String internalCallerIdNumber,
                                 String externalCallerIdNumber,
                                 boolean resetPassword) throws IOException {
        requireNonNull(id, "id must not be null");

        val params = ImmutableMap.<String, String>builder();
        if (nonNull(name))
            params.put(PARAM_NAME, name);

        if (nonNull(internalCallerIdNumber))
            params.put(PARAM_INTERNAL_CALLER_ID_NUMBER, internalCallerIdNumber);

        if (nonNull(externalCallerIdNumber))
            params.put(PARAM_EXTERNAL_CALLER_ID_NUMBER, externalCallerIdNumber);

        if (resetPassword)
            params.put(PARAM_RESET_PASSWORD, Boolean.TRUE.toString());

        return update(id.toString(), params.build(), SipUser.class);
    }

    @Override
    public void deleteSipUser(Long id) throws IOException {
        requireNonNull(id, "id must not be null");
        delete(id.toString());
    }

    @Override
    public Optional<SipUserStatus> getSipUserStatus(Long id) throws IOException {
        requireNonNull(id, "id must not be null");

        Request request = new Request.Builder()
                .url(authenticated()
                        .addPathSegment(id.toString())
                        .addPathSegment("status")
                        .build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            return Optional.of(mapper.readValue(response.body().string(), SipUserStatus.class));
        } catch (HttpResponseException.NotFound e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<SipUser> resetSipUserPassword(Long id) throws IOException {
        requireNonNull(id, "id must not be null");

        Request request = new Request.Builder()
                .url(authenticated()
                        .addPathSegment(id.toString())
                        .addPathSegment("password")
                        .build())
                .post(RequestBody.create("".getBytes()))
                .build();

        try (Response response = client.newCall(request).execute()) {
            return Optional.of(mapper.readValue(response.body().string(), SipUser.class));
        } catch (HttpResponseException.NotFound e) {
            return Optional.empty();
        }
    }
}
