package com.apidaze.sdk.client.externalscripts;

import com.apidaze.sdk.client.common.URL;
import com.apidaze.sdk.client.http.HttpResponseException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * <p>External Scripts are the applications that you build using simple XML responses to calls and messages we send to your application.
 * </p>
 * This interface allows you to manage those scripts.
 */
public interface ExternalScripts {

    /**
     * The maximum length of the script name.
     */
    int MAX_NAME_LENGTH = 40;

    /**
     * Returns the list of external scripts for your domain.
     * @return list of {@link ExternalScript} instances
     * @throws IOException
     * @throws HttpResponseException 
     */
    List<ExternalScript> getExternalScripts() throws IOException;

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
     * @param name a new name, cannot be longer than {@link #MAX_NAME_LENGTH}
     * @param url a new url
     * @return an updated {@link ExternalScript} instance
     * @throws IOException
     * @throws HttpResponseException
     * @throws IllegalArgumentException if the maximum length of the script name has been exceeded
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
}
