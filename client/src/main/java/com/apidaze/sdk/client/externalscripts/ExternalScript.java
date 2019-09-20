package com.apidaze.sdk.client.externalscripts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public
class ExternalScript {
    private Long id;
    private String name;
    private String url;
    private String smsUrl;
    @JsonProperty("reseller_cust_id")
    private Long resellerCustomerId;
    @JsonProperty("dev_cust_id")
    private Long devCustomerId;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}