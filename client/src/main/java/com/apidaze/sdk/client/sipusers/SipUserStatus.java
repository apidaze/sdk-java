package com.apidaze.sdk.client.sipusers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class SipUserStatus {
    String uri;
    String status;

    @JsonCreator
    public SipUserStatus(@JsonProperty("uri") String uri,
                         @JsonProperty("status") String status) {
        this.uri = uri;
        this.status = status;
    }
}
