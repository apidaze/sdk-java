package com.apidaze.sdk.client.messages;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.credentials.Credentials;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import static java.util.Objects.isNull;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AllArgsConstructor
public class MessageClient extends BaseApiClient implements Message {

    private static final String BASE_PATH = "sms";

    private final WebClient client;
    private final Credentials credentials;

    @Builder
    public static MessageClient create(@NotNull Credentials credentials, @Nullable String baseUrl) {
        Assert.notNull(credentials, "Credentials must not be null.");

        if (isNull(baseUrl)) {
            baseUrl = BASE_URL;
        }

        return new MessageClient(WebClient.create(baseUrl), credentials);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String send(PhoneNumber from, PhoneNumber to, String body) {
        Assert.notNull(from, "from must not be null");
        Assert.notNull(from, "to must not be null");
        Assert.hasLength(body, "body must not be empty");

        return client
                .post()
                .uri(withAuthentication()
                        .andThen(uriBuilder -> uriBuilder.pathSegment("send").build()))
                .body(fromFormData("from", from.getNumber())
                        .with("to", to.getNumber())
                        .with("body", body))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    protected String getBasePath() {
        return BASE_PATH;
    }

    @Override
    protected Credentials getCredentials() {
        return credentials;
    }
}
