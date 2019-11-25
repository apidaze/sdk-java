package com.apidaze.sdk.client.applications;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.base.Credentials;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class ApplicationsClient extends BaseApiClient<Application> implements Applications {

    @Getter
    private final String basePath = "applications";
    @Getter
    private final Credentials credentials;
    @Getter
    private final String baseUrl;

    public static ApplicationsClient create(@NotNull Credentials credentials) {
        return create(credentials, DEFAULT_BASE_URL);
    }

    static ApplicationsClient create(@NotNull Credentials credentials, @NotNull String baseUrl) {
        requireNonNull(credentials, "Credentials must not be null.");
        requireNonNull(baseUrl, "baseUrl must not be null.");

        return new ApplicationsClient(credentials, baseUrl);
    }

    @Override
    public List<Application> getApplications() throws IOException {
        return findAll(Application.class);
    }
}
