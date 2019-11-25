package com.apidaze.sdk.client.applications;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;

@Value
@Builder
public class Application {
    Long id;
    Long accountId;
    String applicationId;
    String apiKey;
    String apiSecret;
    String name;
    String fsAddress;
    ZonedDateTime createdAt;
    ZonedDateTime updatedAt;

    @JsonCreator
    public Application(@JsonProperty("id") Long id,
                       @JsonProperty("account_id") Long accountId,
                       @JsonProperty("application_id") String applicationId,
                       @JsonProperty("api_key") String apiKey,
                       @JsonProperty("api_secret") String apiSecret,
                       @JsonProperty("name") String name,
                       @JsonProperty("fs_address") String fsAddress,
                       @JsonProperty("created_at") ZonedDateTime createdAt,
                       @JsonProperty("updated_at") ZonedDateTime updatedAt) {
        this.id = id;
        this.accountId = accountId;
        this.applicationId = applicationId;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.name = name;
        this.fsAddress = fsAddress;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
