package com.apidaze.sdk.xml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import java.time.Duration;
import java.util.List;

import static java.util.Objects.isNull;

@Value
@Builder
public class Playback implements ApidazeScript.Node {

    @NonNull
    @JacksonXmlProperty(isAttribute = true)
    String file;

    @JsonIgnore
    Duration inputTimeout;

    @Singular
    @JacksonXmlProperty(localName = "bind")
    @JacksonXmlElementWrapper(useWrapping = false)
    List<Bind> binds;

    public static Playback fromFile(String file){
        return Playback.builder().file(file).build();
    }

    @JacksonXmlProperty(localName = "input-timeout", isAttribute = true)
    public Long getInputTimeoutMillis() {
        return isNull(inputTimeout) ? null : inputTimeout.toMillis();
    }

    @Override
    public String tag() {
        return "playback";
    }
}
