package com.apidaze.sdk.client.messages;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.base.Credentials;
import lombok.AllArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;

import javax.validation.constraints.NotNull;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AllArgsConstructor(access = PRIVATE)
public class MessageClient extends BaseApiClient implements Message {

    private static final String BASE_PATH = "sms";

    private final WebClient client;
    private final Credentials credentials;

    public static MessageClient create(@NotNull Credentials credentials) {
        return create(credentials, BASE_URL);
    }

    static MessageClient create(@NotNull Credentials credentials, @NotNull String baseUrl) {
        Assert.notNull(credentials, "Credentials must not be null.");
        Assert.notNull(baseUrl, "baseUrl must not be null.");

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
