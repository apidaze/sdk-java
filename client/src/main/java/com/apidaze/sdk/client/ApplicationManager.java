package com.apidaze.sdk.client;

import com.apidaze.sdk.client.applications.Application;
import com.apidaze.sdk.client.applications.Applications;
import com.apidaze.sdk.client.applications.ApplicationsClient;
import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.validates.CredentialsValidator;
import com.apidaze.sdk.client.validates.CredentialsValidatorClient;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class ApplicationManager implements CredentialsValidator, Applications {

    @Getter
    private final ApplicationAction rootApplicationAction;
    private final CredentialsValidator credentialsValidator;
    private final Applications applications;

    /**
     * Initiates an object of this class.
     * @param credentials the root key and secret for the Apidaze account
     * @return An initiated object of this class
     */
    public static ApplicationManager create(Credentials credentials) {
        requireNonNull(credentials, "Credentials must not be null.");

        return new ApplicationManager(
                ApplicationAction.create(credentials),
                CredentialsValidatorClient.create(credentials),
                ApplicationsClient.create(credentials));
    }

    @Override
    public boolean validateCredentials() throws IOException {
        return credentialsValidator.validateCredentials();
    }

    @Override
    public List<Application> getApplications() throws IOException {
        return applications.getApplications();
    }

    @Override
    public Optional<Application> getApplicationById(Long id) throws IOException {
        return applications.getApplicationById(id);
    }

    @Override
    public Optional<Application> getApplicationByApiKey(String apiKey) throws IOException {
        return applications.getApplicationByApiKey(apiKey);
    }

    @Override
    public Optional<Application> getApplicationByName(String name) throws IOException {
        return applications.getApplicationByName(name);
    }
}
