package com.apidaze.sdk.client.cdrhttphandlers;

import com.apidaze.sdk.client.common.URL;
import com.apidaze.sdk.client.http.HttpResponseException;

import java.io.IOException;
import java.util.List;

public interface CdrHttpHandlers {

    /**
     * Get the list of CDR Http Handlers
     * @return list of {@link CdrHttpHandler} instances
     * @throws IOException           if the request could not be executed due to a connectivity problem or timeout.
     * @throws HttpResponseException if REST API returned an unhappy HTTP response code like 404 or 500.
     */
    List<CdrHttpHandler> getCdrHttpHandlers() throws IOException;

    /**
     * Creates a new CDR HTTP Handler. This will post the call detail (after a call) to the webhook URL you define.
     * @param name the name of {@code CdrHttpHandler}
     * @param url the webhook URL to which the call detail will be be post
     * @return created {@link CdrHttpHandler} instance
     * @throws IOException           if the request could not be executed due to a connectivity problem or timeout.
     * @throws HttpResponseException if REST API returned an unhappy HTTP response code like 404 or 500.
     */
    CdrHttpHandler createCdrHttpHandler(String name, URL url) throws IOException;

    /**
     * Update the {@code CdrHttpHandler} name and url
     * @param id id of {@code CdrHttpHandler} to be updated
     * @param name a new name of {@code CdrHttpHandler}
     * @param url a new webhook URL
     * @return updated {@link CdrHttpHandler} instance
     * @throws IOException           if the request could not be executed due to a connectivity problem or timeout.
     * @throws HttpResponseException if REST API returned an unhappy HTTP response code like 404 or 500.
     */
    CdrHttpHandler updateCdrHttpHandler(Long id, String name, URL url) throws IOException;

    /**
     * Update the {@code CdrHttpHandler} name
     * @param id id of {@code CdrHttpHandler} to be updated
     * @param name a new name of {@code CdrHttpHandler}
     * @return updated {@link CdrHttpHandler} instance
     * @throws IOException           if the request could not be executed due to a connectivity problem or timeout.
     * @throws HttpResponseException if REST API returned an unhappy HTTP response code like 404 or 500.
     */
    CdrHttpHandler updateCdrHttpHandlerName(Long id, String name) throws IOException;

    /**
     * Update the {@code CdrHttpHandler} webhook URL
     * @param id id of {@code CdrHttpHandler} to be updated
     * @param url a new webhook URL
     * @return updated {@link CdrHttpHandler} instance
     * @throws IOException           if the request could not be executed due to a connectivity problem or timeout.
     * @throws HttpResponseException if REST API returned an unhappy HTTP response code like 404 or 500.
     */
    CdrHttpHandler updateCdrHttpHandlerUrl(Long id, URL url) throws IOException;

    /**
     * Delete the {@code CdrHttpHandler} instance
     * @param id id of {@code CdrHttpHandler} to be deleted
     * @throws IOException           if the request could not be executed due to a connectivity problem or timeout.
     * @throws HttpResponseException if REST API returned an unhappy HTTP response code like 404 or 500.@throws IOException
     */
    void deleteCdrHttpHandler(Long id) throws IOException;
}
