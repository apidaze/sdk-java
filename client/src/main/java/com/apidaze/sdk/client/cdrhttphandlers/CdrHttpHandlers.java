package com.apidaze.sdk.client.cdrhttphandlers;

import com.apidaze.sdk.client.common.URL;
import com.apidaze.sdk.client.http.HttpResponseException;

import java.io.IOException;
import java.util.List;

public interface CdrHttpHandlers {

    /**
     * Returns the list of CDR Http Handlers
     * @return list of {@link CdrHttpHandler} instances
     * @throws IOException
     * @throws HttpResponseException
     */
    List<CdrHttpHandler> getCdrHttpHandlers() throws IOException;

    /**
     * Creates a new CDR HTTP Handler. This will post the call detail (after a call) to the webhook URL you define.
     * Only one external script is allowed per domain.
     *
     * @param name the name of {@code CdrHttpHandler}
     * @param url the webhook URL to which the call detail will be be post
     * @return created {@link CdrHttpHandler} instance
     * @throws IOException
     * @throws HttpResponseException
     */
    CdrHttpHandler createCdrHttpHandler(String name, URL url) throws IOException;

    /**
     * Updates the name and url of {@code CdrHttpHandler} instance.
     * @param id id of {@code CdrHttpHandler} to be updated
     * @param name a new name of {@code CdrHttpHandler}
     * @param url a new webhook URL
     * @return updated {@link CdrHttpHandler} instance
     * @throws IOException
     * @throws HttpResponseException
     */
    CdrHttpHandler updateCdrHttpHandler(Long id, String name, URL url) throws IOException;

    /**
     * Updates the name of {@code CdrHttpHandler} instance
     * @param id id of {@code CdrHttpHandler} to be updated
     * @param name a new name of {@code CdrHttpHandler}
     * @return updated {@link CdrHttpHandler} instance
     * @throws IOException
     * @throws HttpResponseException
     */
    CdrHttpHandler updateCdrHttpHandlerName(Long id, String name) throws IOException;

    /**
     * Updates the URL of {@code CdrHttpHandler} instance
     * @param id id of {@code CdrHttpHandler} to be updated
     * @param url a new webhook URL
     * @return updated {@link CdrHttpHandler} instance
     * @throws IOException
     * @throws HttpResponseException
     */
    CdrHttpHandler updateCdrHttpHandlerUrl(Long id, URL url) throws IOException;

    /**
     * Deletes an {@code CdrHttpHandler} instance
     * @param id id of {@code CdrHttpHandler} to be deleted
     * @throws IOException
     * @throws HttpResponseException
     */
    void deleteCdrHttpHandler(Long id) throws IOException;
}
