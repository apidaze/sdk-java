package com.apidaze.sdk.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Value
@Builder
@JacksonXmlRootElement(localName = "playback")
public class Playback implements ApidazeScript.Node {

    @JacksonXmlProperty(isAttribute = true)
    String file;

    @Singular
    @JacksonXmlProperty(localName = "bind")
    @JacksonXmlElementWrapper(useWrapping = false)
    List<Bind> binds;

    public static Playback fromFile(String file){
        Objects.requireNonNull(file, "file must not be null");
        return Playback.builder().file(file).build();
    }
}
