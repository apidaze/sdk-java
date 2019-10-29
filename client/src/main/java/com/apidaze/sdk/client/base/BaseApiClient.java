package com.apidaze.sdk.client.base;

import com.apidaze.sdk.client.http.HttpClient;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.val;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;


public abstract class BaseApiClient<T> {

    protected final static String DEFAULT_BASE_URL = "https://api.apidaze.io";

    protected final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .registerModule(new Jdk8Module())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    protected final OkHttpClient client = HttpClient.getClientInstance();

    protected abstract String getBasePath();

    protected abstract String getBaseUrl();

    protected abstract Credentials getCredentials();

    protected HttpUrl authenticatedUrl() {
        return authenticated().build();
    }

    protected HttpUrl.Builder authenticated() {
        return HttpUrl
                .parse(getBaseUrl())
                .newBuilder()
                .addPathSegments(getCredentials().getApiKey())
                .addPathSegment(getBasePath())
                .addQueryParameter("api_secret", getCredentials().getApiSecret());
    }


    protected T create(Map<String, String> params, Class<T> clazz) throws IOException {
        val formBody = new FormBody.Builder();
        params.forEach(formBody::add);

        Request request = new Request.Builder()
                .url(authenticatedUrl())
                .post(formBody.build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return mapper.readValue(response.body().string(), mapper.constructType(clazz));
        }
    }

    protected List<T> findAll(Class<T> clazz) throws IOException {
        Request request = new Request.Builder()
                .url(authenticatedUrl())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return mapper.readValue(response.body().string(), listType(clazz));
        }
    }

    protected T findById(String id, Class<T> clazz) throws IOException {
        requireNonNull(id, "id must not be null");

        Request request = new Request.Builder()
                .url(authenticated()
                        .addPathSegment(id)
                        .build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return mapper.readValue(response.body().string(), mapper.constructType(clazz));
        }
    }

    protected T update(Long id, Map<String, String> params, Class<T> clazz) throws IOException {
        val formBody = new FormBody.Builder();
        params.forEach(formBody::add);

        Request request = new Request.Builder()
                .url(authenticated()
                        .addPathSegment(id.toString())
                        .build())
                .put(formBody.build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return mapper.readValue(response.body().string(), mapper.constructType(clazz));
        }
    }

    protected void delete(String id) throws IOException {
        Request request = new Request.Builder()
                .url(authenticated().addPathSegment(id).build())
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        }
    }

    private JavaType listType(Class<T> clazz) {
        return mapper.getTypeFactory().constructCollectionType(List.class, clazz);
    }
}
