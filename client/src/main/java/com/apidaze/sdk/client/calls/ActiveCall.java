package com.apidaze.sdk.client.calls;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class ActiveCall {
    UUID uuid;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created;
    String callerIdName;
    String callerIdNumber;
    String destination;
    Calls.State callState;
    String callUuid;
    String callerId;
    String url;
    String workTag;

    @JsonCreator
    public ActiveCall(@JsonProperty("uuid") UUID uuid,
                      @JsonProperty("created") LocalDateTime created,
                      @JsonProperty("cid_name") String callerIdName,
                      @JsonProperty("cid_num") String callerIdNumber,
                      @JsonProperty("dest") String destination,
                      @JsonProperty("callstate") Calls.State callState,
                      @JsonProperty("call_uuid") String callUuid,
                      @JsonProperty("callerid") String callerId,
                      @JsonProperty("URL") String url,
                      @JsonProperty("work_tag") String workTag) {
        this.uuid = uuid;
        this.created = created;
        this.callerIdName = callerIdName;
        this.callerIdNumber = callerIdNumber;
        this.destination = destination;
        this.callState = callState;
        this.callUuid = callUuid;
        this.callerId = callerId;
        this.url = url;
        this.workTag = workTag;
    }
}
