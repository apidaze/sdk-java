package com.apidaze.sdk.client;

import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.validates.CredentialsValidator;
import com.apidaze.sdk.client.validates.CredentialsValidatorClient;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class ApplicationManager implements CredentialsValidator {

    @Getter
    private final ApplicationAction applicationAction;
    private final CredentialsValidator credentialsValidator;

    public static ApplicationManager create(Credentials credentials) {
        requireNonNull(credentials, "Credentials must not be null.");

        return new ApplicationManager(
                ApplicationAction.create(credentials),
                CredentialsValidatorClient.create(credentials));
    }

    @Override
    public boolean validateCredentials() throws IOException {
        return credentialsValidator.validateCredentials();
    }
}
