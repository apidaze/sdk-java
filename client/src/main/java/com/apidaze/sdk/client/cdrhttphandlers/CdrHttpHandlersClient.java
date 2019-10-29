package com.apidaze.sdk.client.cdrhttphandlers;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.base.Credentials;
import com.apidaze.sdk.client.common.URL;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import static java.util.Objects.requireNonNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CdrHttpHandlersClient extends BaseApiClient<CdrHttpHandler> implements CdrHttpHandlers {

    @Getter
    private final String basePath = "cdrhttphandlers";
    @Getter
    private final Credentials credentials;
    @Getter
    private final String baseUrl;

    public static CdrHttpHandlersClient create(@NotNull Credentials credentials) {
        return create(credentials, DEFAULT_BASE_URL);
    }

    static CdrHttpHandlersClient create(@NotNull Credentials credentials, @NotNull String baseUrl) {
        requireNonNull(credentials, "Credentials must not be null.");
        requireNonNull(baseUrl, "baseUrl must not be null.");

        return new CdrHttpHandlersClient(credentials, baseUrl);
    }


    @Override
    public List<CdrHttpHandler> getCdrHttpHandlers() throws IOException {
        return findAll(CdrHttpHandler.class);
    }

    @Override
    public CdrHttpHandler createCdrHttpHandler(String name, URL url) throws IOException {
        requireNonNull(name, "name must not be null");
        requireNonNull(url, "url must not be null");

        val params = ImmutableMap.of("name", name, "url", url.getValue());
        return create(params, CdrHttpHandler.class);
    }
}
