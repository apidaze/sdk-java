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
     *
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
    public List<Application> getApplicationsById(Long id) throws IOException {
        return applications.getApplicationsById(id);
    }

    @Override
    public List<Application> getApplicationsByApiKey(String apiKey) throws IOException {
        return applications.getApplicationsByApiKey(apiKey);
    }

    @Override
    public List<Application> getApplicationsByName(String name) throws IOException {
        return applications.getApplicationsByName(name);
    }

    /**
     * @param id application id
     * @return a new instance of {@code ApplicationAction} associated with application id, if it exists
     * @throws IOException
     */
    public Optional<ApplicationAction> getApplicationActionById(Long id) throws IOException {
        return applications.getApplicationsById(id)
                .stream()
                .findFirst()
                .map(app -> ApplicationAction.create(app.getCredentials()));
    }

    /**
     * @param apiKey api key
     * @return a new instance of {@code ApplicationAction} associated with api key, if it exists
     * @throws IOException
     */
    public Optional<ApplicationAction> getApplicationActionByApiKey(String apiKey) throws IOException {
        return applications.getApplicationsByApiKey(apiKey)
                .stream()
                .findFirst()
                .map(app -> ApplicationAction.create(app.getCredentials()));
    }

    /**
     * @param name application name unique to account
     * @return a new instance of {@code ApplicationAction} associated with application name, if it exists for given account
     * @throws IOException
     */
    public Optional<ApplicationAction> getApplicationActionByName(String name) throws IOException {
        return applications.getApplicationsByName(name)
                .stream()
                .findFirst()
                .map(app -> ApplicationAction.create(app.getCredentials()));
    }
}
