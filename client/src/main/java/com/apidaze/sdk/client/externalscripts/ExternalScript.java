package com.apidaze.sdk.client.externalscripts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;

@Value
@Builder
public class ExternalScript {
    Long id;
    String name;
    URL url;
    URL smsUrl;
    Long resellerCustomerId;
    Long devCustomerId;
    ZonedDateTime createdAt;
    ZonedDateTime updatedAt;

    @JsonCreator
    public ExternalScript(@JsonProperty("id") Long id,
                          @JsonProperty("name") String name,
                          @JsonProperty("url") URL url,
                          @JsonProperty("sms_url") URL smsUrl,
                          @JsonProperty("reseller_cust_id") Long resellerCustomerId,
                          @JsonProperty("dev_cust_id") Long devCustomerId,
                          @JsonProperty("created_at") ZonedDateTime createdAt,
                          @JsonProperty("updated_at") ZonedDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.smsUrl = smsUrl;
        this.resellerCustomerId = resellerCustomerId;
        this.devCustomerId = devCustomerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}