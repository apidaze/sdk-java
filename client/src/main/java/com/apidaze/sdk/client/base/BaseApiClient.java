package com.apidaze.sdk.client.base;

import com.apidaze.sdk.client.http.HttpClient;
import com.apidaze.sdk.client.http.HttpResponseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.ImmutableMap;
import lombok.val;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;
import static java.util.Objects.nonNull;


/**
 * <p>This is a <i>base</i> class for building Apidaze REST API clients.
 * This class uses a shareable instance of <code>OkHttpClient</code> and supports CRUD methods.
 * </p>
 * @param <T> A type parameter which defines the type of entity managed the specific client implementation.
 */
public abstract class BaseApiClient<T> {

    /**
     * The default base URL used to communicate with Apidaze REST API
     */
    protected final static String DEFAULT_BASE_URL = "https://api.apidaze.io";

    protected final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .registerModule(new Jdk8Module())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    protected final OkHttpClient client = HttpClient.getClientInstance();

    /**
     * @return A base path of the resource managed by the client implementation.
     */
    protected abstract String getBasePath();

    /**
     * @return A base URL used to communicate with Apidaze REST API.
     */
    protected abstract String getBaseUrl();

    /**
     * @return {@link Credentials}
     */
    protected abstract Credentials getCredentials();

    /**
     * @return The {@code HttpUrl} with baseUrl, apiKey, apiSecret and basePath included.
     */
    protected HttpUrl authenticatedUrl() {
        return authenticated().build();
    }

    /**
     * @return The {@code HttpUrl.Builder} with baseUrl, apiKey, apiSecret and basePath included.
     */
    protected HttpUrl.Builder authenticated() {
        return authenticated(emptyMap());
    }

    /**
     * Creates the authenticated {@code HttpUrl.Builder} with provided parameters.
     * @return The {@code HttpUrl.Builder} with paramters, baseUrl, apiKey, apiSecret and basePath included.
     */
    protected HttpUrl.Builder authenticated(Map<String, String> parameters) {
        val url = HttpUrl
                .parse(getBaseUrl())
                .newBuilder()
                .addPathSegments(getCredentials().getApiKey())
                .addPathSegment(getBasePath())
                .addQueryParameter("api_secret", getCredentials().getApiSecret());

        if (nonNull(parameters)) {
            parameters.forEach(url::addQueryParameter);
        }

        return url;
    }

    /**
     * Creates an entity of type {@code T} by sending POST request to {@link #getBasePath()}
     * @param params a map with parameters names an values which are passed as HTTP form parameters
     * @param clazz the class object of type {@code T} used to deserialize JSON content
     * @return a created entity
     * @throws IOException
     */
    protected T create(Map<String, String> params, Class<T> clazz) throws IOException {
        val formBody = new FormBody.Builder();

        if (nonNull(params)) {
            params.forEach(formBody::add);
        }

        Request request = new Request.Builder()
                .url(authenticatedUrl())
                .post(formBody.build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            return mapper.readValue(response.body().string(), mapper.constructType(clazz));
        }
    }

    /**
     * Returns the list of entities of type {@code T} by sending GET request to {@link #getBasePath()}
     * @param clazz the class object of type {@code T} used to deserialize JSON content
     * @return the list of entities
     * @throws IOException
     */
    protected List<T> findAll(Class<T> clazz) throws IOException {
        Request request = new Request.Builder()
                .url(authenticatedUrl())
                .build();

        try (Response response = client.newCall(request).execute()) {
            return mapper.readValue(response.body().string(), listType(clazz));
        }
    }

    /**
     * Returns the list of entities of type {@code T} by sending GET request with a given query parameter
     * @param name the query parameter name
     * @param value the query parameter value
     * @param clazz the class object of type {@code T} used to deserialize JSON content
     * @return the list of entities
     * @throws IOException
     */
    protected List<T> findByParameter(String name, String value, Class<T> clazz) throws IOException {
        val params = ImmutableMap.of(name, value);
        return findByParameters(params, clazz);
    }

    /**
     * Returns the list of entities of type {@code T} by sending GET request with a given query parameters
     * @param parameters the map containing query parameters to be sent in GET request
     * @param clazz the class object of type {@code T} used to deserialize JSON content
     * @return the list of entities
     * @throws IOException
     */
    protected List<T> findByParameters(Map<String, String> parameters, Class<T> clazz) throws IOException {
        Request request = new Request.Builder()
                .url(authenticated(parameters).build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            return mapper.readValue(response.body().string(), listType(clazz));
        }
    }


    /**
     * Returns an entity of type {@code T} by sending GET request to {@link #getBasePath()}/{@code id}
     * @param id id of the entity to find
     * @param clazz the class object of type {@code T} used to deserialize JSON content
     * @return an entity of type {@code T} wrapped by {@code Optional} if present, otherwise an empty {@code Optional}
     * @throws IOException
     */
    protected Optional<T> findById(String id, Class<T> clazz) throws IOException {
        Request request = new Request.Builder()
                .url(authenticated()
                        .addPathSegment(id)
                        .build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            return Optional.of(mapper.readValue(response.body().string(), mapper.constructType(clazz)));
        } catch (HttpResponseException.NotFound e) {
            return Optional.empty();
        }
    }

    /**
     * Modifies an entity of type {@code T} by sending PUT request to {@link #getBasePath()}/{@code id}
     * @param id id of the entity to modify
     * @param params a map with parameters names an values which are passed as HTTP form parameters
     * @param clazz the class object of type {@code T} used to deserialize JSON content
     * @return a updated entity
     * @throws IOException
     */
    protected T update(String id, Map<String, String> params, Class<T> clazz) throws IOException {
        val formBody = new FormBody.Builder();
        params.forEach(formBody::add);

        Request request = new Request.Builder()
                .url(authenticated()
                        .addPathSegment(id)
                        .build())
                .put(formBody.build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            return mapper.readValue(response.body().string(), mapper.constructType(clazz));
        }
    }

    /**
     * Deletes an entity of type {@code T} by sending DELETE request to {@link #getBasePath()}/{@code id}
     * @param id id of the entity to delete
     * @throws IOException
     */
    protected void delete(String id) throws IOException {
        Request request = new Request.Builder()
                .url(authenticated().addPathSegment(id).build())
                .delete()
                .build();

        try (Response ignored = client.newCall(request).execute()) {}
    }

    private JavaType listType(Class<T> clazz) {
        return mapper.getTypeFactory().constructCollectionType(List.class, clazz);
    }
}
