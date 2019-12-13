package com.apidaze.sdk.xml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.NonNull;
import lombok.Value;

import java.time.Duration;


@Value
public class Echo implements ApidazeScript.Node {

    @JsonIgnore
    @NonNull
    Duration delay;

    @JacksonXmlText
    public long getDelayMillis() {
        return delay.toMillis();
    }

    @Override
    public String tag() {
        return "echo";
    }
}
