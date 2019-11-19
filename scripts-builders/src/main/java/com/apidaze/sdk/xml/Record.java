package com.apidaze.sdk.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class Record implements ApidazeScript.Node {

    @JacksonXmlProperty(isAttribute = true)
    String name;

    @JacksonXmlProperty(localName = "on-answered", isAttribute = true)
    Boolean onAnswered;

    @JacksonXmlProperty(localName = "aleg", isAttribute = true)
    Boolean aLeg;

    @JacksonXmlProperty(localName = "bleg", isAttribute = true)
    Boolean bLeg;

    @Override
    public String tag() {
        return "record";
    }
}
