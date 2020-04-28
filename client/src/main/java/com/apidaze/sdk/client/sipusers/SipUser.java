package com.apidaze.sdk.client.sipusers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;

@Value
@Builder
public class SipUser {
    Long id;
    String name;
    CallerId callerId;
    Sip sip;
    ZonedDateTime createdAt;
    ZonedDateTime updatedAt;


    @JsonCreator
    public SipUser(@JsonProperty("id") Long id,
                   @JsonProperty("name") String name,
                   @JsonProperty("callerid") CallerId callerId,
                   @JsonProperty("sip") Sip sip,
                   @JsonProperty("created_at") ZonedDateTime createdAt,
                   @JsonProperty("updated_at") ZonedDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.callerId = callerId;
        this.sip = sip;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Value
    @Builder
    public static class CallerId {
        String outboundCallerIdName;
        String outboundCallerIdNumber;
        String internalCallerIdName;
        String internalCallerIdNumber;

        @JsonCreator
        public CallerId(@JsonProperty("outboundCallerIdName") String outboundCallerIdName,
                        @JsonProperty("outboundCallerIdNumber") String outboundCallerIdNumber,
                        @JsonProperty("internalCallerIdName") String internalCallerIdName,
                        @JsonProperty("internalCallerIdNumber") String internalCallerIdNumber) {
            this.outboundCallerIdName = outboundCallerIdName;
            this.outboundCallerIdNumber = outboundCallerIdNumber;
            this.internalCallerIdName = internalCallerIdName;
            this.internalCallerIdNumber = internalCallerIdNumber;
        }
    }

    @Value
    public static class Sip {
        String username;
        String password;

        @JsonCreator
        public Sip(@JsonProperty("username") String username,
                   @JsonProperty("password") String password) {
            this.username = username;
            this.password = password;
        }
    }
}
