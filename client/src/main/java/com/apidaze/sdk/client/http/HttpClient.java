package com.apidaze.sdk.client.http;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import okhttp3.OkHttpClient;

import static java.time.Duration.ofSeconds;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpClient {

    private final static long CONNECTION_TIMEOUT_SECONDS = 10;
    private final static long READ_TIMEOUT_SECONDS = 30;
    private final static long WRITE_TIMEOUT_SECONDS = 30;
    private final static long CALL_TIMEOUT_SECONDS = 60;

    private static class HttpClientHelper {
        private static final OkHttpClient CLIENT_INSTANCE = new OkHttpClient.Builder()
                .connectTimeout(ofSeconds(CONNECTION_TIMEOUT_SECONDS))
                .readTimeout(ofSeconds(READ_TIMEOUT_SECONDS))
                .writeTimeout(ofSeconds(WRITE_TIMEOUT_SECONDS))
                .callTimeout(ofSeconds(CALL_TIMEOUT_SECONDS))
                .addInterceptor(new HttpErrorHandler())
                .build();
    }

    public static OkHttpClient getClientInstance() {
        return HttpClientHelper.CLIENT_INSTANCE;
    }
}
