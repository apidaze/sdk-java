package com.apidaze.sdk.client.mediafiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;

@Value
@Builder
public class MediaFile {
    String name;
    Long size;
    ZonedDateTime modified;

    @JsonCreator
    public MediaFile(@JsonProperty("name") String name,
                     @JsonProperty("size") Long size,
                     @JsonProperty("modified") ZonedDateTime modified) {
        this.name = name;
        this.size = size;
        this.modified = modified;
    }
}
