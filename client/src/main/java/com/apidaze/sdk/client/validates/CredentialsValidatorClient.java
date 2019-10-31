package com.apidaze.sdk.client.validates;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.http.HttpResponseException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class CredentialsValidatorClient extends BaseApiClient implements CredentialsValidator {

    @Getter
    private final String basePath = "validates";
    @Getter
    private final Credentials credentials;
    @Getter
    private final String baseUrl;

    public static CredentialsValidatorClient create(Credentials credentials) {
        return create(credentials, DEFAULT_BASE_URL);
    }

    static CredentialsValidatorClient create(Credentials credentials, String baseUrl) {
        requireNonNull(credentials, "Credentials must not be null.");
        requireNonNull(baseUrl, "baseUrl must not be null.");

        return new CredentialsValidatorClient(credentials, baseUrl);
    }

    @Override
    public boolean validateCredentials() throws IOException {
        Request request = new Request.Builder()
                .url(authenticatedUrl())
                .build();

        try (Response ignored = client.newCall(request).execute()) {
            return Boolean.TRUE;
        } catch (HttpResponseException.Unauthorized e) {
            return Boolean.FALSE;
        }
    }
}
