package com.apidaze.sdk.client.validates;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.base.Credentials;
import lombok.AllArgsConstructor;
import lombok.Getter;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class CredentialsValidator extends BaseApiClient {

    @Getter
    private final String basePath = "validates";
    @Getter
    private final Credentials credentials;
    @Getter
    private final String baseUrl;

    public static CredentialsValidator create(Credentials credentials) {
        return create(credentials, DEFAULT_BASE_URL);
    }

    static CredentialsValidator create(Credentials credentials, String baseUrl) {
        requireNonNull(credentials, "Credentials must not be null.");
        requireNonNull(baseUrl, "baseUrl must not be null.");

        return new CredentialsValidator(credentials, baseUrl);
    }

    public boolean validateCredentials() throws IOException {
        Request request = new Request.Builder()
                .url(authenticatedUrl())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return Boolean.TRUE;
            } else if (response.code() == 404 || response.code() == 401) {
                return Boolean.FALSE;
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }
    }
}
