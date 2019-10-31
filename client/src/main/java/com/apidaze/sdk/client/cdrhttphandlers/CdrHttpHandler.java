package com.apidaze.sdk.client.cdrhttphandlers;

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
    String url;
    CallLeg callLeg;
    ZonedDateTime createdAt;
    ZonedDateTime updatedAt;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CdrHttpHandler(@JsonProperty("id") Long id,
                          @JsonProperty("name") String name,
                          @JsonProperty("format") Format format,
                          @JsonProperty("url") String url,
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
        OUTBOUND("outbound");

        @JsonValue
        @Getter
        private final String value;
    }

    @AllArgsConstructor(access = PRIVATE)
    public enum Format {
        REGULAR("regular"),
        CSV("csv"),
        XML("xml"),
        JSON("json");

        @JsonValue
        @Getter
        private final String value;
    }

}
