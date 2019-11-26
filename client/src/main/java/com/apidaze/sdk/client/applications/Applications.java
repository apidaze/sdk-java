package com.apidaze.sdk.client.applications;

import com.apidaze.sdk.client.http.HttpResponseException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface Applications {

    List<Application> getApplications() throws IOException;

    /**
     * Returns an application details retrieved by application id.
     * @param id id of the application to fetch
     * @return an application details, otherwise {@code Optional.empty()}
     * @throws IOException
     * @throws HttpResponseException
     */
    Optional<Application> getApplicationById(Long id) throws IOException;

    /**
     * Returns an application details retrieved by api key.
     * @param apiKey api key of the application to fetch
     * @return an application details, otherwise {@code Optional.empty()}
     * @throws IOException
     * @throws HttpResponseException
     */
    Optional<Application> getApplicationByApiKey(String apiKey) throws IOException;

    /**
     * Returns an application details retrieved by api key.
     * @param name the name of the application to fetch
     * @return an application details, otherwise {@code Optional.empty()}
     * @throws IOException
     * @throws HttpResponseException
     */
    Optional<Application> getApplicationByName(String name) throws IOException;
}
