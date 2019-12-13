package com.apidaze.sdk.client;

import com.apidaze.sdk.client.common.URL;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.Parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.apidaze.sdk.client.TestUtil.API_KEY;
import static com.apidaze.sdk.client.TestUtil.API_SECRET;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpMethod.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.Parameter.param;
import static org.mockserver.model.ParameterBody.params;

public abstract class GenericRequest {

    private static final String PARAM_NAME = "name";
    private static final String PARAM_URL = "url";
    private static final String PARAM_API_SECRET = "api_secret";
    private static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";

    protected abstract String getBasePath();

    protected HttpRequest getAll() {
        return request()
                .withMethod(GET.name())
                .withPath("/" + API_KEY + "/" + getBasePath())
                .withQueryStringParameters(param(PARAM_API_SECRET, API_SECRET));
    }

    protected HttpRequest getById(@NotNull UUID id) {
        return getById(id.toString());
    }

    protected HttpRequest getById(@NotNull Long id) {
        return getById(id.toString());
    }

    protected HttpRequest getById(@NotNull String id) {
        return request()
                .withMethod(GET.name())
                .withPath("/" + API_KEY + "/" + getBasePath() + "/" + id)
                .withQueryStringParameters(param(PARAM_API_SECRET, API_SECRET));
    }

    protected HttpRequest getByParameter(@NotNull String name, String value) {
        return request()
                .withMethod(GET.name())
                .withPath("/" + API_KEY + "/" + getBasePath())
                .withQueryStringParameters(
                        param(PARAM_API_SECRET, API_SECRET),
                        param(name, value));
    }

    protected HttpRequest create(@NotNull Map<String, String> params) {
        List<Parameter> parameters = new ArrayList<>();
        params.forEach((k, v) -> parameters.add(new Parameter(k, v)));

        return request()
                .withMethod(POST.name())
                .withHeader(CONTENT_TYPE, APPLICATION_FORM_URLENCODED)
                .withPath("/" + API_KEY + "/" + getBasePath())
                .withQueryStringParameters(param(PARAM_API_SECRET, API_SECRET))
                .withBody(params(parameters));
    }

    protected HttpRequest update(@NotNull Long id, @NotNull Map<String, String> params) {
        return update(id.toString(), params);
    }

    protected HttpRequest update(@NotNull String id, @NotNull Map<String, String> params) {
        List<Parameter> parameters = new ArrayList<>();
        params.forEach((k, v) -> parameters.add(new Parameter(k, v)));

        return request()
                .withMethod(PUT.name())
                .withHeader(CONTENT_TYPE, APPLICATION_FORM_URLENCODED)
                .withPath("/" + API_KEY + "/" + getBasePath() + "/" + id)
                .withQueryStringParameters(param(PARAM_API_SECRET, API_SECRET))
                .withBody(params(parameters));
    }

    protected HttpRequest delete(@NotNull Long id) {
        return delete(id.toString());
    }

    protected HttpRequest delete(@NotNull String id) {
        return request()
                .withMethod(DELETE.name())
                .withPath("/" + API_KEY + "/" + getBasePath() + "/" + id)
                .withQueryStringParameters(param(PARAM_API_SECRET, API_SECRET));
    }

    protected HttpRequest create(String name, URL url) {
        return create(ImmutableMap.of(PARAM_NAME, name, PARAM_URL, url.getValue()));
    }

    protected HttpRequest update(@NotNull Long id, String newName, @NotNull URL newUrl) {
        return update(id, ImmutableMap.of(PARAM_NAME, newName, PARAM_URL, newUrl.getValue()));
    }

    protected HttpRequest updateUrl(@NotNull Long id, @NotNull URL newUrl) {
        return update(id, ImmutableMap.of(PARAM_URL, newUrl.getValue()));
    }

    protected HttpRequest updateName(@NotNull Long id, @NotNull String newName) {
        return update(id, ImmutableMap.of(PARAM_NAME, newName));
    }
}
