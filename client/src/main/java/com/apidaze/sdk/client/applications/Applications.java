package com.apidaze.sdk.client.applications;

import com.apidaze.sdk.client.http.HttpResponseException;

import java.io.IOException;
import java.util.List;

public interface Applications {

    List<Application> getApplications() throws IOException;

    /**
     * Returns an application details retrieved by application id.
     * @param id id of the application to fetch
     * @return a list containing one element with application details if it exists, otherwise an empty list
     * @throws IOException
     * @throws HttpResponseException
     */
    List<Application> getApplicationsById(Long id) throws IOException;

    /**
     * Returns an application details retrieved by api key.
     * @param apiKey api key of the application to fetch
     * @return a list containing one element with application details if it exists, otherwise an empty list
     * @throws IOException
     * @throws HttpResponseException
     */
    List<Application> getApplicationsByApiKey(String apiKey) throws IOException;

    /**
     * Returns an application details retrieved by api key.
     * @param name the name of the application to fetch
     * @return a list containing one element with application details if it exists, otherwise an empty list
     * @throws IOException
     * @throws HttpResponseException
     */
    List<Application> getApplicationsByName(String name) throws IOException;
}
