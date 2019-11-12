package com.apidaze.sdk.client.validates;

import com.apidaze.sdk.client.http.HttpResponseException;

import java.io.IOException;

public interface CredentialsValidator {

    /**
     * Validates the credentials.
     *
     * @return {@code true} if credentials are valid, otherwise {@code false}
     * @throws IOException
     * @throws HttpResponseException
     */
    boolean validateCredentials() throws IOException;
}
