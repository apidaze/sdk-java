package com.apidaze.sdk.client.externalscripts;

import com.apidaze.sdk.client.common.URL;
import com.apidaze.sdk.client.http.HttpResponseException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ExternalScripts {

    /**
     * Returns the list of external scripts for your domain.
     * @return list of {@link ExternalScript} instances
     * @throws IOException
     * @throws HttpResponseException 
     */
    List<ExternalScript> getExternalScripts() throws IOException;

    /**
     * Creates a new external script. Only one external script is allowed per domain.
     * @param name the name of {@code ExternalScript}
     * @param url the webhook URL to which the call detail will be be post
     * @return a created {@link ExternalScript} instance
     * @throws IOException
     * @throws HttpResponseException
     */
    ExternalScript createExternalScript(String name, URL url) throws IOException;

    /**
     * Returns an external script object details by id.
     * @param id id of external script to fetch
     * @return an external script object if present, otherwise {@code Optional.empty()}
     * @throws IOException
     * @throws HttpResponseException
     */
    Optional<ExternalScript> getExternalScript(Long id) throws IOException;

    /**
     * Updates the name and url of {@code ExternalScript} instance.
     * @param id id of external script to be updated
     * @param name a new name
     * @param url a new url
     * @return an updated {@link ExternalScript} instance
     * @throws IOException
     * @throws HttpResponseException
     */
    ExternalScript updateExternalScript(Long id, String name, URL url) throws IOException;

    /**
     * Updates the name and url of {@code ExternalScript} instance.
     * @param id id of external script to be updated
     * @param url a new url
     * @return an updated {@link ExternalScript} instance
     * @throws IOException
     * @throws HttpResponseException
     */
    ExternalScript updateExternalScriptUrl(Long id, URL url) throws IOException;

    /**
     * Deletes an {@code ExternalScript} instance.
     * @param id id of external script to be deleted
     * @throws IOException
     * @throws HttpResponseException
     */
    void deleteExternalScript(Long id) throws IOException;
}
