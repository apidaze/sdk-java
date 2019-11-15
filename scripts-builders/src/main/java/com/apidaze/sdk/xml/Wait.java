package com.apidaze.sdk.xml;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Value;

import java.time.Duration;

@Value
public class Wait implements ApidazeScript.Node {

    @JsonIgnore
    Duration value;

    @JacksonXmlText
    public long getValueSeconds() {
        return value.getSeconds();
    }

    @Override
    public String tag() {
        return "wait";
    }
}
