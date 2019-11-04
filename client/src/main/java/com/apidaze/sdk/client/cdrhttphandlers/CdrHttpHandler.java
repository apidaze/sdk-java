package com.apidaze.sdk.client.cdrhttphandlers;

import com.apidaze.sdk.client.common.URL;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.time.ZonedDateTime;

import static lombok.AccessLevel.PRIVATE;

@Value
@Builder
public class CdrHttpHandler {
    Long id;
    String name;
    Format format;
    URL url;
    CallLeg callLeg;
    ZonedDateTime createdAt;
    ZonedDateTime updatedAt;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CdrHttpHandler(@JsonProperty("id") Long id,
                          @JsonProperty("name") String name,
                          @JsonProperty("format") Format format,
                          @JsonProperty("url") URL url,
                          @JsonProperty("call_leg") CallLeg callLeg,
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

    @AllArgsConstructor(access = PRIVATE)
    public enum CallLeg {
        INBOUND("inbound"),
        OUTBOUND("outbound"),
        BOTH("both");

        @JsonValue
        @Getter
        private final String value;
    }

    @AllArgsConstructor(access = PRIVATE)
    public enum Format {
        REGULAR("regular"),
        JSON("json"),
        XML("xml");

        @JsonValue
        @Getter
        private final String value;
    }

}
