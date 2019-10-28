package com.apidaze.sdk.client.cdrhttphandlers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;

@Value
@Builder
public class CdrHttpHandler {
    Long id;
    String name;
    String format; // TODO maybe enum?
    String url;
    String callLeg; // TODO maybe enum?
    ZonedDateTime createdAt;
    ZonedDateTime updatedAt;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CdrHttpHandler(@JsonProperty("id") Long id,
                          @JsonProperty("name") String name,
                          @JsonProperty("format") String format,
                          @JsonProperty("url") String url,
                          @JsonProperty("call_leg") String callLeg,
                          @JsonProperty("created_at") ZonedDateTime createdAt,
                          @JsonProperty("updated_at") ZonedDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.format = format;
        this.url = url;
        this.callLeg = callLeg;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
