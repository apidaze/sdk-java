package com.apidaze.sdk.client.messages;

import com.apidaze.sdk.client.base.BaseApiClient;
import com.apidaze.sdk.client.base.Credentials;
import lombok.AllArgsConstructor;
import lombok.Getter;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class MessageClient extends BaseApiClient implements Message {

    @Getter
    private final String basePath = "sms";
    @Getter
    private final Credentials credentials;
    @Getter
    private final String baseUrl;

    public static MessageClient create(Credentials credentials) {
        return create(credentials, DEFAULT_BASE_URL);
    }

    static MessageClient create(Credentials credentials, String baseUrl) {
        requireNonNull(credentials, "Credentials must not be null.");
        requireNonNull(baseUrl, "baseUrl must not be null.");

        return new MessageClient(credentials, baseUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String send(PhoneNumber from, PhoneNumber to, String body) throws IOException {
        requireNonNull(from, "from must not be null");
        requireNonNull(to, "to must not be null");

        if (isNullOrEmpty(body)) throw new IllegalArgumentException("body must not be null or empty");

        RequestBody formBody = new FormBody.Builder()
                .add("from", from.getNumber())
                .add("to", to.getNumber())
                .add("body", body)
                .build();

        Request request = new Request.Builder()
                .url(authenticated().addPathSegment("send").build())
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }
}
